package com.example.springsecuritydemo.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 后台操作员，登录后台管理
 */
@Data
public class Operator implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主健ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 登录TOKEN
     */
    @Deprecated
    private String token;

    /**
     * 用户状态
     */
    private Integer status;

}
