package com.example.springsecuritypermit.config;

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
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private DataSource dataSource;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        // 指定认证管理器，通过UserDetailsService查询用户信息
        AuthenticationManagerBuilder managerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = managerBuilder.build();

        // 没有权限跳转到no-permission.html页面
        httpSecurity.exceptionHandling().accessDeniedPage("/no-permission.html");

        httpSecurity
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        //.requestMatchers("/users.html").hasAuthority("8") //hasAuthority方法：当前登陆用户，只有具有8权限才可以访问这个路径
                        //.requestMatchers("/users.html").hasAnyAuthority("2","9")
                        //.requestMatchers("/users.html").hasRole("3") // 当前登陆用户具备给定角色就允许访问
                        .requestMatchers("/users.html").hasAnyRole("1", "3")
                        .anyRequest().authenticated())
                .authenticationManager(authenticationManager)
                .formLogin()
                .loginPage("/login.html").permitAll() // 设置自定义登陆页面
                .loginProcessingUrl("/login") // 登陆时访问的路径
                .failureUrl("/error.html") // 登陆失败的页面
                .defaultSuccessUrl("/index.html") // 登陆成功后跳转的路径
                .and()
                .rememberMe() // 开启记住我的功能
                .tokenRepository(jdbcTokenRepository()) // 持久化令牌的方案
                .tokenValiditySeconds(60*60*24*7) // 设置令牌的有效期，以秒为单位
                .and()
                .logout().logoutUrl("/logout") // 注销接口
                .logoutSuccessUrl("/login.html") // 注销成功后跳转的页面
                .and()
                .csrf().disable(); // 需要关闭csrf防护功能，否则登陆不成功

        return httpSecurity.build();
    }

    @Bean
    public JdbcTokenRepositoryImpl jdbcTokenRepository(){
        // 配置数据源
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        // 自动在数据库创建权限表（再次启动会再次创建表，所以会报错，表示表已经存在）
        // jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
