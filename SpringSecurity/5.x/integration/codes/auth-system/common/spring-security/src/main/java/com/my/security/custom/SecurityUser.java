package com.my.security.custom;

import com.my.model.po.system.SysUser;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 将SysUser包装为SpringSecurity下的User，用于SpringSecurity的认证和授权过程
 */
@Getter
@Setter
public class SecurityUser extends User {

    private SysUser sysUser;

    public SecurityUser(SysUser sysUser, Collection<? extends GrantedAuthority> authorities) {
        // 调用父类的构造方法，传入用户名，密码，权限列表
        super(sysUser.getUsername(), sysUser.getPassword(), authorities);
        this.sysUser = sysUser;
    }

}
