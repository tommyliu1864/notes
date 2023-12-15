package com.example.springsecurityjwt.model.po;

import lombok.Data;

/**
 * 系统用户（真正跟数据库表字段对应的实体类）
 */
@Data
public class SysUser {

    private Integer id;

    private String username;

    private String password;


}
