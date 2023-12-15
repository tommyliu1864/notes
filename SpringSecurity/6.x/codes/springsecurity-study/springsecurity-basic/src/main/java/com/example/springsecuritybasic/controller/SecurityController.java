package com.example.springsecuritybasic.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {
    @RequestMapping("/hello")
    public String hello() {
        return "hello security";
    }
}
