package com.my.common.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class Result<T> {

    @ApiModelProperty("返回状态码")
    private Integer code;

    @ApiModelProperty("返回状态的消息")
    private String message;

    @ApiModelProperty("返回数据")
    private T data;

    public static <T> Result<T> build(ResultCodeEnum resultCodeEnum, T data) {
        Result<T> result = new Result<>();
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        result.setData(data);
        return result;
    }

    public static <T> Result<T> ok(T data) {
        return build(ResultCodeEnum.SUCCESS, data);
    }

    public static <T> Result<T> ok() {
        return build(ResultCodeEnum.SUCCESS, null);
    }

    public static <T> Result<T> fail(T data) {
        return build(ResultCodeEnum.FAIL, data);
    }

    public static <T> Result<T> fail() {
        return build(ResultCodeEnum.FAIL, null);
    }

    public static <T> Result<T> fail(ResultCodeEnum resultCodeEnum) {
        return build(resultCodeEnum, null);
    }

    public Result<T> code(int code) {
        setCode(code);
        return this;
    }

    public Result<T> message(String msg) {
        setMessage(msg);
        return this;
    }
}
