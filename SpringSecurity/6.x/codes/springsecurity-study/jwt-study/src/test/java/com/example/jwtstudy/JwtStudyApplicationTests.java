package com.example.jwtstudy;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class JwtStudyApplicationTests {

    @Test
    void testCreateToken() {
        // 创建JWT对象
        JwtBuilder jwtBuilder = Jwts.builder()
                // 设置负载内容
                // 用户的ID
                .setId("1001")
                // 用户名
                .setSubject("小明")
                // JWT 的签发时间
                .setIssuedAt(new Date())
                // 设置签名算法和盐
                .signWith(SignatureAlgorithm.HS256, "java01");
        // 获取JWT对象中的字符串
        String token = jwtBuilder.compact();
        System.out.println(token);
    }

    @Test
    public void testParseToken() {
        //String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMDAxIiwic3ViIjoi5bCP5piOIiwiaWF0IjoxNjg2MTIwOTg3fQ.KZsoZeqD5qVNwEsRV7jAYCVzVTR_CyXwYyuDhSzUj_U";
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMDAxIiwic3ViIjoi5bCP5piOIiwiaWF0IjoxNjg2MTIyMjE1LCJyb2xlIjoiYWRtaW4ifQ.a2KzPDT4GhavcSen85ECNvRhR9rDRc7U2m5cLDM8G_I";
        // 解析Token，生成Claims对象，Token中存放的用户信息解析到了claims对象中
        Claims claims = Jwts.parser().setSigningKey("java01").parseClaimsJws(token).getBody();
        System.out.println("id:" + claims.getId());
        System.out.println("subject:" + claims.getSubject());
        System.out.println("IssuedAt:" + claims.getIssuedAt());
        System.out.println("role:" + claims.get("role"));
    }


    @Test
    void testExpToken() {
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId("1001")
                .setSubject("小明")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "java01")
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 1000)); // 设置10秒之后超时
        // 获取JWT对象中的字符串
        String token = jwtBuilder.compact();
        System.out.println(token);
    }


    @Test
    void testCustomClaims() {
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId("1001")
                .setSubject("小明")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "java01")
                .claim("role", "admin");
        String token = jwtBuilder.compact();
        System.out.println(token);
    }
}
