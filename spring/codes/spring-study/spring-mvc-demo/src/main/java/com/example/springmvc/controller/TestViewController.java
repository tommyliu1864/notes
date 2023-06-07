package com.example.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestViewController {

    @RequestMapping("/testHello")
    public String testHello() {
        return "hello";
    }

    @RequestMapping("/testForward")
    public String testForward() {
        return "forward:/testHello";
    }

    @RequestMapping("/testRedirect")
    public String testRedirect(){
        return "redirect:/testHello";
    }


}
