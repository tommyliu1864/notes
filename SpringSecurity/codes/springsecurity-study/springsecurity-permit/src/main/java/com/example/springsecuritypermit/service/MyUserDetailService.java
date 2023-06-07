package com.example.springsecuritypermit.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.springsecuritypermit.mapper.SysUserMapper;
import com.example.springsecuritypermit.pojo.SysUser;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j2
public class MyUserDetailService implements UserDetailsService {
    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(username), SysUser::getUsername, username);
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        User user = null;
        if (Objects.nonNull(sysUser)) {
            // 获取用户的所有权限
            Set<Integer> permissions = sysUserMapper.getAllPermissions(sysUser.getId());
            String permissionText = permissions.stream().map(String::valueOf).collect(Collectors.joining(","));
            log.info("permissionText:{}", permissionText);
            // 获取用户的所有角色
            Set<Integer> roles = sysUserMapper.getAllRoles(sysUser.getId());
            String roleText = roles.stream().map(num -> "ROLE_" + num).collect(Collectors.joining(","));
            log.info("roleText:{}", roleText);
            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(permissionText + "," + roleText);
            user = new User(username, sysUser.getPassword(), authorities);
        }
        return user;
    }
}
