package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    @Transactional
    public void deductBalance(Long id, Integer money) {
        // 查询用户
        User user = getById(id);
        // 判断用户状态
        if (user == null && user.getStatus().getValue() == 2) {
            throw new RuntimeException("用户状态异常");
        }
        // 判断用户余额
        if (user.getBalance() < money) {
            throw new RuntimeException("用户余额不足");
        }
        int remainBalance = user.getBalance() - money;
        lambdaUpdate()
                .set(User::getBalance, remainBalance)
                .set(remainBalance == 0, User::getStatus, 2)
                .eq(User::getId, id)
                .eq(User::getBalance, user.getBalance())
                .update();
    }

    @Override
    public UserVO queryUserAndAddressById(Long userId) {
        // 1.查询用户
        User user = getById(userId);
        if (user == null) {
            return null;
        }
        // 2.查询收获地址
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .eq(Address::getUserId, userId)
                .list();
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        userVO.setAddresses(BeanUtil.copyToList(addresses, AddressVO.class));
        return userVO;
    }

    @Override
    public List<UserVO> queryUserAndAddressByIds(List<Long> ids) {
        //1. 查询用户
        List<User> users = listByIds(ids);
        if (CollUtil.isEmpty(users)) {
            return Collections.emptyList();
        }
        // 2.根据用户ID查询出所有地址信息
        // 2.1 获取已查出的用户ID集合
        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        // 2.2 根据用户ID查询地址
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .in(Address::getUserId, userIds)
                .list();
        // 2.3 转换地址vo
        List<AddressVO> addressVOS = BeanUtil.copyToList(addresses, AddressVO.class);
        Map<Long, List<AddressVO>> addressMap = new HashMap<>(0);
        if (CollUtil.isNotEmpty(addressVOS)) {
            // 把地址按照用户ID分组
            addressMap = addressVOS.stream().collect(Collectors.groupingBy(AddressVO::getUserId));
        }
        // 3.转换vo返回
        List<UserVO> list = new ArrayList<>(users.size());
        for (User user : users) {
            UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
            list.add(userVO);
            // 根据用户ID取出对应的地址列表
            userVO.setAddresses(addressMap.get(user.getId()));
        }
        return list;
    }

    @Override
    public PageDTO<UserVO> queryUsersPage(UserQuery query) {
        // 1. 构建条件
        // 1.1 分页条件
        Page<User> page = query.toMpPageDefaultSortByCreateTimeDesc();

        // 2. 分页查询
        lambdaQuery()
                .like(query.getName() != null, User::getUsername, query.getName())
                .eq(query.getStatus() != null, User::getStatus, query.getStatus())
                .ge(query.getMinBalance() != null, User::getBalance, query.getMinBalance())
                .le(query.getMaxBalance() != null, User::getBalance, query.getMaxBalance())
                .page(page);

        // 3.封装返回
        return PageDTO.of(page, user -> {
            // 拷贝属性到VO
            UserVO vo = BeanUtil.copyProperties(user, UserVO.class);
            // 用户名脱敏
            String username = vo.getUsername();
            vo.setUsername(username.substring(0, username.length() - 2) + "**");
            return vo;
        });
    }
}
