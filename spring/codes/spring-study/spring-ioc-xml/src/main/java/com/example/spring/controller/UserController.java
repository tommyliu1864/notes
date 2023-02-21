package com.example.spring.controller;

import com.example.spring.pojo.User;
import com.example.spring.service.UserService;

public class UserController {

    private UserService userService;

    public void save(){
        User user = new User("admin", "123456", 20);
        userService.save(user);
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
