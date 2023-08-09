package com.example.springsecurityjwt.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * 向客户端响应的工具类
 */
public class ResponseUtil {

    public static void out(HttpServletResponse response, ResultModel resultModel) {
        ObjectMapper objectMapper = new ObjectMapper();
        // 设置response的状态码和内容格式
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // 响应的内容
        try {
            objectMapper.writeValue(response.getOutputStream(), resultModel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
