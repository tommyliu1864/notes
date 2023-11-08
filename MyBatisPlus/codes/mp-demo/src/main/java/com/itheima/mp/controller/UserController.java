package com.itheima.mp.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "用户管理接口")
@RestController
@RequestMapping("users")
public class UserController {
    @Resource
    private IUserService userService;

    @PostMapping
    @ApiOperation("新增用户")
    public void saveUser(@RequestBody UserFormDTO userFormDTO) {
        User user = BeanUtil.copyProperties(userFormDTO, User.class);
        userService.save(user);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除用户")
    public void removeUserById(@PathVariable("id") Long userId) {
        userService.removeById(userId);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询用户")
    public UserVO queryUserById(@PathVariable("id") Long userId) {
        //User user = userService.getById(userId);
        //return BeanUtil.copyProperties(user, UserVO.class);
        return userService.queryUserAndAddressById(userId);
    }

    @GetMapping
    @ApiOperation("根据id集合查询用户")
    public List<UserVO> queryUserById(@RequestParam("ids") List<Long> ids) {
        //List<User> users = userService.listByIds(ids);
        ///return BeanUtil.copyToList(users, UserVO.class);
        return userService.queryUserAndAddressByIds(ids);
    }

    @PutMapping("{id}/deduction/{money}")
    @ApiOperation("扣减用户余额")
    public void deductBalance(@PathVariable("id") Long id, @PathVariable("money") Integer money) {
        userService.deductBalance(id, money);
    }

    @GetMapping("/list")
    @ApiOperation("根据条件查询用户")
    public List<UserVO> queryUsers(UserQuery query) {
        String username = query.getName();
        Integer status = query.getStatus();
        Integer minBalance = query.getMinBalance();
        Integer maxBalance = query.getMaxBalance();

        List<User> users = userService.lambdaQuery()
                .like(username != null, User::getUsername, username)
                .eq(status != null, User::getStatus, status)
                .ge(minBalance != null, User::getBalance, minBalance)
                .le(maxBalance != null, User::getBalance, maxBalance)
                .list();

        return BeanUtil.copyToList(users, UserVO.class);
    }


    @GetMapping("/page")
    @ApiOperation("用户信息分页查询")
    public PageDTO<UserVO> queryUsersPage(UserQuery query){
        return userService.queryUsersPage(query);
    }

}
