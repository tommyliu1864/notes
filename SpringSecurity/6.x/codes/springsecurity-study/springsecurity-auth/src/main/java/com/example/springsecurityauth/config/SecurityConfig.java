package com.example.springsecurityauth.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Resource
    private UserDetailsService userDetailsService;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // 指定认证管理器，通过UserDetailsService查询用户信息
        AuthenticationManagerBuilder managerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = managerBuilder.build();

        // 表单认证
        httpSecurity.authorizeHttpRequests()
                .anyRequest()
                .authenticated()
                .and()
                .authenticationManager(authenticationManager)
                .formLogin()
                .loginPage("/login.html")
                .permitAll()
                // 当登陆成功后，是否指定跳转到首页
                .defaultSuccessUrl("/index.html", true)
                // post请求的登陆接口
                .loginProcessingUrl("/login")
                // 登陆失败，用户名或密码错误
                .failureUrl("/error.html")
                .and()
                // 注销接口
                .logout().logoutUrl("/logout")
                // 注销成功后跳转的页面
                .logoutSuccessUrl("/login.html").permitAll()
                .and()
                // 需要关闭csrf防护功能，否则登陆不成功
                .csrf().disable();

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
