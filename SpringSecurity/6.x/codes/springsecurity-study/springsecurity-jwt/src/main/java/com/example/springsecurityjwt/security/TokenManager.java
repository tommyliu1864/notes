package com.example.springsecurityjwt.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * token管理器
 */
@Component
public class TokenManager {

    // token的私钥
    @Value("{token.securityKey}")
    private String securityKey;

    // 过期时间为7天
    private long expireTime = 1000 * 60 * 60 * 24 * 7;

    /**
     * 使用JWT生成token
     *
     * @param username
     * @return
     */
    public String createToken(String username) {
        return Jwts.builder().setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(SignatureAlgorithm.HS256, securityKey).compact();
    }
}
