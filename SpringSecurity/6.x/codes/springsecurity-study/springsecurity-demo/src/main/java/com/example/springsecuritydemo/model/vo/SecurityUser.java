package com.example.springsecuritydemo.model.vo;

import com.example.springsecuritydemo.model.po.Operator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * SpringSecurity中的用户信息UserDetails，它实际成了User的包装类
 */
public class SecurityUser implements UserDetails {

    // 当前登录用户
    private Operator currentUser;
    // 权限列表
    private Set<Integer> permissionValues;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 将字符串集合转换为SimpleGrantedAuthority集合，并返回
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (Integer value : permissionValues) {
            if (!StringUtils.isEmpty(value)) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(String.valueOf(value));
                authorities.add(authority);
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return currentUser.getPassword();
    }

    @Override
    public String getUsername() {
        return currentUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setCurrentUser(Operator currentUser) {
        this.currentUser = currentUser;
    }

    public Set<Integer> getPermissionValues() {
        return permissionValues;
    }

    public void setPermissionValues(Set<Integer> permissionValues) {
        this.permissionValues = permissionValues;
    }
}
