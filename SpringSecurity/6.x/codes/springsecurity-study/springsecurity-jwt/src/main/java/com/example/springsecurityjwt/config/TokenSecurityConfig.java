package com.example.springsecurityjwt.config;

import com.example.springsecurityjwt.filter.TokenLoginFilter;
import com.example.springsecurityjwt.security.TokenManager;
import com.example.springsecurityjwt.security.UnAuthEntryPoint;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//@EnableMethodSecurity
public class TokenSecurityConfig{

    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private TokenManager tokenManager;
    @Resource
    private RedisTemplate redisTemplate;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // 要求所有接口都需要做权限认证
        httpSecurity.authorizeHttpRequests(authorizeHttpRequests ->
                authorizeHttpRequests.anyRequest().authenticated());

        // 指定认证管理器，通过UserDetailsService查询用户信息
        AuthenticationManagerBuilder managerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = managerBuilder.build();

        // 处理登陆的逻辑
        httpSecurity.addFilter(new TokenLoginFilter(authenticationManager, tokenManager, redisTemplate));

        // 没有权限时的处理方案
        httpSecurity.exceptionHandling(exceptionHandlingConfigurer ->
                exceptionHandlingConfigurer.authenticationEntryPoint(new UnAuthEntryPoint()));

        return httpSecurity.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}
