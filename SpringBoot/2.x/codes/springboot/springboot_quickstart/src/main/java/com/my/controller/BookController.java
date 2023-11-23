package com.my.controller;

import com.my.Enterprise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {

    @Value("${lesson}")
    private String lesson;
    @Value("${center.dataDir}")
    private String dataDir;

    @GetMapping
    public String getById() {
        System.out.println("springboot is running...");
        System.out.println(lesson);
        return "Springboot is running.";
    }

}
