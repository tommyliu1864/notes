package com.example.springsecurityjwt.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * UserDetails中的当前用户（currentUser）对象
 */
@Data
public class User implements Serializable {

    private String username;
    private String password;
    private String token;

}
