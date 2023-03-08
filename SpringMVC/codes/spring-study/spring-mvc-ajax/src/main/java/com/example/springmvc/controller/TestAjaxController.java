package com.example.springmvc.controller;

import com.example.springmvc.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Controller
@RestController
public class TestAjaxController {

    /*@PostMapping("/test/RequestBody/json")
    public void testRequestBody(@RequestBody Map<String, Object> map, HttpServletResponse response) throws IOException {
        System.out.println("map = " + map);
        response.getWriter().print("hello,axios");
    }*/

    @PostMapping("/test/ResponseBody/json")
    //@ResponseBody
    public User testResponseBody() {
        User user = new User("admin", "123");
        return user;
    }

    /*@PostMapping("/test/ResponseBody/json")
    @ResponseBody
    public List<User> testResponseBody() {
        User user1 = new User("admin", "123");
        User user2 = new User("root", "123");
        User user3 = new User("xxx", "123");
        List<User> list = Arrays.asList(user1, user2, user3);
        return list;
    }*/

    /*@PostMapping("/test/ResponseBody/json")
    @ResponseBody
    public Map<String, Object> testResponseBody() {
        User user1 = new User("admin", "123");
        User user2 = new User("root", "123");
        User user3 = new User("xxx", "123");
        Map<String, Object> map = new HashMap<>();
        map.put("1001", user1);
        map.put("1002", user2);
        map.put("1003", user3);
        return map;
    }*/
}
