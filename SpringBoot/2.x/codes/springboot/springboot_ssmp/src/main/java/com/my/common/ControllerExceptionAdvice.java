package com.my.common;

import com.my.vo.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 当Controller捕捉到异常时，统一向前端返回一个异常信息
 */
@RestControllerAdvice
public class ControllerExceptionAdvice {

    // 拦截所有异常信息
    @ExceptionHandler(Exception.class)
    public R doException(Exception e) {
        // 可以记录日志
        e.printStackTrace();
        return new R(false, null, "服务器异常！");
    }

}
