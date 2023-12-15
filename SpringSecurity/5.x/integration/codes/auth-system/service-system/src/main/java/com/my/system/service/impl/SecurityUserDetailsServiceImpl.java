package com.my.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.my.common.result.ResultCodeEnum;
import com.my.system.dao.SysMenuMapper;
import com.my.system.dao.SysUserMapper;
import com.my.model.po.system.SysMenu;
import com.my.model.po.system.SysUser;
import com.my.security.custom.SecurityUser;
import com.my.system.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * SpringSecurity在做用户认证时，需要通过UserDetailsService查询出用户信息
 */
@Slf4j
@Component
public class SecurityUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;

    /**
     * 通过用户名查询出用户信息，并用于认证
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<SysUser> wrapper = new QueryWrapper<SysUser>().lambda()
                .eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserMapper.selectOne(wrapper);
        if (sysUser == null) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST.getCode(), ResultCodeEnum.USER_NOT_EXIST.getMessage());
        }
        if (sysUser.getStatus() != 1) {
            throw new CustomException(ResultCodeEnum.USER_LOCKED.getCode(), ResultCodeEnum.USER_LOCKED.getMessage());
        }
        // 获取用户权限列表
        Long userId = sysUser.getId();
        // 超级管理员，则查询出所有权限
        List<SysMenu> sysMenus = null;
        if (userId == 1) {
            LambdaQueryWrapper<SysMenu> sysMenuWrapper = new QueryWrapper<SysMenu>().lambda()
                    .eq(SysMenu::getStatus, 1);
            sysMenus = sysMenuMapper.selectList(sysMenuWrapper);
        } else {
            // 不是超级管理员，则只查出他对应的权限
            sysMenus = sysUserMapper.getAuthorities(userId);
        }
        List<SimpleGrantedAuthority> authorities = sysMenus.stream()
                .map(SysMenu::getPerms)
                .filter(StringUtils::hasText)  // 从SysMenu集合中提取一个字符串集合
                .map(SimpleGrantedAuthority::new) // 转为SimpleGrantedAuthority集合
                .collect(Collectors.toList());
        log.debug("authorities from database:" + authorities.toString());
        return new SecurityUser(sysUser, authorities);
    }
}
