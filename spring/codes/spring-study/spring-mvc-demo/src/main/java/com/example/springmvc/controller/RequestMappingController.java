package com.example.springmvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class RequestMappingController {

    //@RequestMapping(value = "test?")
    //匹配：/test1，/test123
    //@RequestMapping(value = "test*")
    //匹配：/test，/test1，/test123
    //@RequestMapping(value = "/**/test")
    //匹配：/test，/aa/bb/test
    /*public String testRequestMapping() {
        return "success";
    }*/

    /*@GetMapping(value = {"/testRequestMapping", "test"})
    public String testRequestMapping() {
        return "success";
    }*/

    @RequestMapping("/testRest/{id}/{username}")
    public String testRest(@PathVariable(value = "id") String id, @PathVariable(value = "username") String username) {
        System.out.println("id = " + id + ", username = " + username);
        return "success";
    }

    @RequestMapping("testServletApi")
    public String testServletApi(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("username = " + username + ",password:" + password);
        return "success";
    }

    /*@RequestMapping("testParam")
    public String testParam(String username, String password) {
        System.out.println("username = " + username + ",password:" + password);
        return "success";
    }*/

    @RequestMapping("testParam")
    public String testParam(
            @RequestParam(
                    value = "userName",
                    required = false,
                    defaultValue = "admin"
            ) String username, String password,
            //@RequestHeader("referer") String referer,
            @CookieValue("JSESSIONID") String jsessionId) {
        System.out.println("username = " + username + ",password:" + password);
        //System.out.println("referer = " + referer);
        System.out.println("jsessionId = " + jsessionId);
        return "success";
    }
}
