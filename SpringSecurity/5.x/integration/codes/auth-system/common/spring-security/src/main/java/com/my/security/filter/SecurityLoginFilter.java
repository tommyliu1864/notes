package com.my.security.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.common.result.Result;
import com.my.common.result.ResultCodeEnum;
import com.my.common.utils.IpUtil;
import com.my.common.utils.JwtHelper;
import com.my.common.utils.ResponseUtil;
import com.my.model.po.system.SysLoginLog;
import com.my.model.vo.system.LoginVO;
import com.my.security.custom.SecurityUser;
import com.my.security.service.SysLoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * SpringSecurity认证（用户登录）
 */
@Slf4j
public class SecurityLoginFilter extends UsernamePasswordAuthenticationFilter {

    private StringRedisTemplate redisTemplate;

    private SysLoginLogService sysLoginLogService;

    public SecurityLoginFilter(
            AuthenticationManager authenticationManager,
            StringRedisTemplate redisTemplate,
            SysLoginLogService sysLoginLogService) {
        setAuthenticationManager(authenticationManager);
        setPostOnly(false);
        // 指定登录接口的地址
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/system/index/login", "POST"));
        this.redisTemplate = redisTemplate;
        this.sysLoginLogService = sysLoginLogService;
    }

    /**
     * 认证（登录）
     *
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginVO loginVO = new ObjectMapper().readValue(request.getInputStream(), LoginVO.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginVO.getUsername(), loginVO.getPassword());
            return this.getAuthenticationManager().authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 登录成功，生成token并返回
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityUser user = (SecurityUser) authResult.getPrincipal();
        String token = JwtHelper.createToken(user.getSysUser().getId(), user.getSysUser().getUsername());
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        // 权限数据保存到redis中，后续需要使用
        redisTemplate.opsForValue().set(user.getUsername(), JSON.toJSONString(user.getAuthorities()));
        log.debug("authorities to redis:" + JSON.toJSONString(user.getAuthorities()));
        // 保存登录成功日志
        SysLoginLog loginLog = new SysLoginLog(user.getUsername(), IpUtil.getIpAddress(request), 1, "登录成功");
        sysLoginLogService.save(loginLog);
        ResponseUtil.out(response, Result.ok(map));
    }

    // 登录失败
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // 登录失败会引发AuthenticationException异常，我们要获取引发AuthenticationException异常的原始异常
        // 并向客户端输出错误信息
        // 我们在loadUserByUsername方法中抛出的CustomException，都是RuntimeException
        if (failed.getCause() instanceof RuntimeException) {
            // 直接输出异常信息
            ResponseUtil.out(response, Result.fail().message(failed.getCause().getMessage()));
        } else {
            // 否则，输出用户登录失败
            ResponseUtil.out(response, Result.fail(ResultCodeEnum.USER_LOGIN_FAILED));
        }
    }
}
