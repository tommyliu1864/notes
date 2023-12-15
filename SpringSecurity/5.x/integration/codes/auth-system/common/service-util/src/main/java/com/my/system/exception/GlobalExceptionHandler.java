package com.my.system.exception;

import com.my.common.result.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    // 全局异常处理
    @ExceptionHandler(Exception.class)
    public Result caught(Exception e) {
        e.printStackTrace();
        return Result.fail().message("服务器内部错误");
    }

    // 自定义异常处理
    @ExceptionHandler(CustomException.class)
    public Result caught(CustomException e) {
        e.printStackTrace();
        // 从异常信息中获取code和message，设置到响应结果中
        return Result.fail().code(e.getCode()).message(e.getMessage());
    }

    // SpringSecurity拦截非法访问，会抛出异常
    @ExceptionHandler(AccessDeniedException.class)
    public Result caught(AccessDeniedException e) {
        e.printStackTrace();
        // 从异常信息中获取code和message，设置到响应结果中
        return Result.fail().message(e.getMessage());
    }


}
