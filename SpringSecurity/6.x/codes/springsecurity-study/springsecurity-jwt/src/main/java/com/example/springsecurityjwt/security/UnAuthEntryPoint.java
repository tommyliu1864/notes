package com.example.springsecurityjwt.security;

import com.example.springsecurityjwt.common.ResponseUtil;
import com.example.springsecurityjwt.common.ResultModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class UnAuthEntryPoint implements AuthenticationEntryPoint {

    // 权限认证失败执行的方法
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseUtil.out(response, ResultModel.error());
    }
}
