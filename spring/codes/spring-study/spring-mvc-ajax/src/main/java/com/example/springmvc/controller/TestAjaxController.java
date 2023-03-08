package com.example.springmvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestAjaxController {

    @PostMapping("/test/RequestBody/json")
    public String testRequestBody(@RequestBody String requestBody) {
        System.out.println("requestBody = " + requestBody);
        return "success";
    }

}
