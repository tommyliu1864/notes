package com.my.security.filter;

import com.alibaba.fastjson.JSON;
import com.my.common.result.Result;
import com.my.common.result.ResultCodeEnum;
import com.my.common.utils.JwtHelper;
import com.my.common.utils.ResponseUtil;
import com.my.model.vo.system.AuthorityVO;
import com.my.system.exception.CustomException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 对系统的每一个请求进行过滤
 * 1.如果是访问登录接口，直接放行
 * 2.访问其他接口则需要从token中解析用户信息（包含权限信息），用户信息将给到SpringSecurity做授权使用
 */
public class SecurityAuthenticationFilter extends OncePerRequestFilter {

    private static final String LOGIN_URL = "/admin/system/index/login";

    private StringRedisTemplate redisTemplate;

    public SecurityAuthenticationFilter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 登录接口，放行
        if (LOGIN_URL.equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        // 有可能用户已经登录过了，所以可以从token解析出用户信息，然后把权限信息给到SpringSecurity去做
        Authentication authentication = getAuthentication(request, response);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } else {
            // token为空，则提示非法访问
            ResponseUtil.out(response, Result.fail(ResultCodeEnum.ILLEGAL_ACCESS));
        }
    }

    /**
     * 从token中解析出用户信息
     *
     * @param request
     * @param response
     * @return
     */
    private Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("token");
        String username = null;
        if (StringUtils.hasText(token)) {
            try {
                username = JwtHelper.getUsername(token);
            } catch (ExpiredJwtException e) {
                // 登录信息超时，向客户端返回超时信息
                ResponseUtil.out(response, Result.fail(ResultCodeEnum.TOKEN_EXPIRE));
                throw e;
            }
            // 从redis读取用户权限
            String json = redisTemplate.opsForValue().get(username);
            if (json == null){
                // redis重启会导致缓存被清空，直接让用户重新登录
                // 实际上这里应该从数据库重新读取数据加入到缓存中
                ResponseUtil.out(response, Result.fail(ResultCodeEnum.TOKEN_EXPIRE));
                return null;
            }
            List<SimpleGrantedAuthority> authorities = JSON.parseArray(json, AuthorityVO.class)
                    .stream()
                    .map(AuthorityVO::getAuthority)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        }
        return null;
    }
}
