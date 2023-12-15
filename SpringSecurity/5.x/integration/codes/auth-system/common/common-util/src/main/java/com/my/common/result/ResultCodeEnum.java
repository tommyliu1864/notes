package com.my.common.result;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {
    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    USER_NOT_EXIST(202, "用户名不存在"),
    USERNAME_OR_PASSWORD_WRONG(203, "用户名或密码错误"),
    USER_LOCKED(204, "用户已被冻结"),
    USER_LOGIN_FAILED(205, "用户登录失败"),
    ILLEGAL_ACCESS(206, "非法访问"),
    TOKEN_EXPIRE(207, "登录信息超时，请重新登录")
    ;

    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
