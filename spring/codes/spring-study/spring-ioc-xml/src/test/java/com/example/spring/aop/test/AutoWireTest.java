package com.example.spring.aop.test;

import com.example.spring.aop.controller.UserController;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AutoWireTest {

    @Test
    public void testAutoWireByXML(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-autowire.xml");
        UserController userController = applicationContext.getBean(UserController.class);
        userController.save();
    }

}
