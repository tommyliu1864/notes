package com.example.springsecuritydemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.springsecuritydemo.mapper.OperatorMapper;
import com.example.springsecuritydemo.model.po.Operator;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 登录验证时AuthenticationProvider 通过UserDetailsService加载出用户信息
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private OperatorMapper operatorMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<Operator> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(username), Operator::getUsername, username);
        Operator operator = operatorMapper.selectOne(queryWrapper);
        if (operator == null){
            throw new UsernameNotFoundException("找不到该用户");
        }
        // 查询出用户权限列表
        return null;
    }
}
