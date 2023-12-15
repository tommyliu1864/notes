package com.example.springsecurityjwt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.springsecurityjwt.mapper.SysUserMapper;
import com.example.springsecurityjwt.model.po.SysUser;
import com.example.springsecurityjwt.model.vo.SecurityUser;
import com.example.springsecurityjwt.model.vo.User;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(username), SysUser::getUsername, username);
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        if (Objects.isNull(sysUser)) {
            throw new UsernameNotFoundException("当前用户不存在");
        }
        User user = new User();
        BeanUtils.copyProperties(sysUser, user);

        // 根据用户名从数据库查询到该用户的权限信息
        Set<Integer> permissions = sysUserMapper.getAllPermissions(sysUser.getId());
        SecurityUser securityUser = new SecurityUser();
        securityUser.setCurrentUser(user);
        securityUser.setPermissionValues(permissions);
        return securityUser;
    }
}
