package com.my.common.utils;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Date;

/**
 * 生成JSON Web令牌的工具类
 */
public class JwtHelper {

    // token过期时间
    private static long tokenExpiration = Duration.ofDays(1).toMillis();

    // 加密秘钥
    private static final String tokenSignKey = "X90&y%6702p";

    /**
     * @param userId
     * @param username
     * @return
     */
    public static String createToken(Long userId, String username) {
        return Jwts.builder()
                .setSubject("AUTH-USER")
                .claim("userId", String.valueOf(userId))
                .claim("username", username)
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .compressWith(CompressionCodecs.GZIP)
                .compact();
    }

    /**
     * 从token解析出userId
     *
     * @param token
     * @return
     */
    public static Long getUserId(String token) {
        if (!StringUtils.hasText(token)) return null;
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        return Long.parseLong(claimsJws.getBody().get("userId").toString());
    }

    /**
     * 从token解析出username
     *
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        if (!StringUtils.hasText(token)) return null;
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        return (String) claimsJws.getBody().get("username");
    }

}
