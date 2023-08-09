package com.example.springsecurityjwt.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 一个实体类，用于向客户端返回消息
 *
 * @param <T>
 */
@Data
public class ResultModel<T> implements Serializable {

    // 状态码
    // 200表示成功 500表示认证失败
    private int code;
    // 消息
    private String message;
    // 数据
    private T result;

    public static ResultModel success() {
        ResultModel resultModel = new ResultModel();
        resultModel.setCode(200);
        resultModel.setMessage("success");
        resultModel.setResult(null);
        return resultModel;
    }

    public static ResultModel success(String data) {
        ResultModel resultModel = new ResultModel();
        resultModel.setCode(200);
        resultModel.setMessage("success");
        resultModel.setResult(data);
        return resultModel;
    }

    public static ResultModel error() {
        ResultModel resultModel = new ResultModel();
        resultModel.setCode(500);
        resultModel.setMessage("error");
        resultModel.setResult(null);
        return resultModel;
    }

    public static ResultModel error(String message) {
        ResultModel resultModel = new ResultModel();
        resultModel.setCode(500);
        resultModel.setMessage(message);
        resultModel.setResult(null);
        return resultModel;
    }

    public static ResultModel error(int code, String message) {
        ResultModel resultModel = new ResultModel();
        resultModel.setCode(code);
        resultModel.setMessage(message);
        resultModel.setResult(null);
        return resultModel;
    }
}
