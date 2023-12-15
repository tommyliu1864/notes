package com.my.security.custom;

import cn.hutool.crypto.SecureUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 密码加密器
 */
@Component
public class Sha1PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return SecureUtil.sha1(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(SecureUtil.sha1(rawPassword.toString()));
    }
}
