package com.example.springsecurityjwt.filter;

import com.example.springsecurityjwt.common.ResponseUtil;
import com.example.springsecurityjwt.common.ResultModel;
import com.example.springsecurityjwt.model.vo.SecurityUser;
import com.example.springsecurityjwt.model.vo.User;
import com.example.springsecurityjwt.security.TokenManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 登录（认证）过滤器
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;

    public TokenLoginFilter(AuthenticationManager authenticationManager, TokenManager tokenManager, RedisTemplate redisTemplate) {
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
        // 经过这个filter，允许post以外的其他请求
        this.setPostOnly(false);
        // 设置登陆的路径和请求方式
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/user/login", "POST"));
    }

    /**
     * 执行认证的方法
     *
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 获取表单提交的用户名密码信息
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            User user = objectMapper.readValue(request.getInputStream(), User.class);
            // 校验，认证的过程
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), new ArrayList<>())
            );
            return authenticate;
        } catch (IOException e) {
            throw new RuntimeException("认证失败！");
        }
    }

    /**
     * 认证成功以后调用的方法
     * 登录成功之后，需要将用户名生成token，存入redis中，然后将token返回给客户端，客户端后续请求需要带上该token
     *
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // 获取用户名
        SecurityUser securityUser = (SecurityUser) authResult.getPrincipal();
        String username = securityUser.getUsername();
        // 生成token
        String token = tokenManager.createToken(username);
        // 将token存入redis，键为token，值为权限列表
        redisTemplate.opsForValue().set(username, securityUser.getPermissionValues());
        // 向客户端返回token
        ResponseUtil.out(response, ResultModel.success(token));
    }

    /**
     * 认证失败调用的方法
     *
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        ResponseUtil.out(response, ResultModel.error(401, failed.getMessage()));
    }
}
