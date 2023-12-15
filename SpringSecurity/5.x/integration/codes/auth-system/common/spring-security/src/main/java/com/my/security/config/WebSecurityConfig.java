package com.my.security.config;

import com.my.security.custom.Sha1PasswordEncoder;
import com.my.security.filter.SecurityAuthenticationFilter;
import com.my.security.filter.SecurityLoginFilter;
import com.my.security.service.SysLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // 开启SpringSecurity的默认行为
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启注解功能
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private Sha1PasswordEncoder sha1PasswordEncoder;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SysLoginLogService sysLoginLogService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // 跨站请求伪造保护（Cross-site request forgery），暂时不需要
                .cors() // 开启跨域请求
                .and().authorizeRequests()
                .antMatchers("/admin/system/index/login").permitAll() // 登录接口不需要认证
                .anyRequest().authenticated() // 其它接口都需要认证
                .and().addFilterBefore(new SecurityAuthenticationFilter(redisTemplate), UsernamePasswordAuthenticationFilter.class) // 把拦截加在认证过滤器之前
                .addFilter(new SecurityLoginFilter(authenticationManager(), redisTemplate, sysLoginLogService));
        // 禁用session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    // 指定密码加密器
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(sha1PasswordEncoder);
    }

    // 指定哪些请求不拦截
    @Override
    public void configure(WebSecurity web) throws Exception {
        // swagger 有关的请求，放行
        web.ignoring().antMatchers("/favicon.ico", "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/doc.html");
    }
}
