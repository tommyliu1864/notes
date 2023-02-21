package com.example.spring.controller;


import com.example.spring.pojo.User;
import com.example.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    /*@Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }*/

    /*@Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }*/

    public void save(){
        User user = new User("Jack", "123456", 20);
        userService.save(user);
    }
}
