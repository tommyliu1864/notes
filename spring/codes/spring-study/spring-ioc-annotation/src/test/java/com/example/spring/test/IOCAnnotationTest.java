package com.example.spring.test;

import com.example.spring.controller.UserController;
import com.example.spring.dao.UserDao;
import com.example.spring.service.UserService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class IOCAnnotationTest {

    @Test
    public void testAutowireByAnnotation() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-ioc-annotation.xml");
        UserController userController = applicationContext.getBean(UserController.class);
        System.out.println(userController);
        UserService userService = applicationContext.getBean(UserService.class);
        System.out.println(userService);
        UserDao userDao = applicationContext.getBean(UserDao.class);
        System.out.println(userDao);
    }

    @Test
    public void testAutowireByAnnotation2() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-ioc-annotation.xml");
        UserController userController = (UserController) applicationContext.getBean("userController");
        System.out.println(userController);
    }

    @Test
    public void testAutowireByAnnotation3() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-ioc-annotation.xml");
        UserController userController = (UserController) applicationContext.getBean("userController");
        userController.save();
    }
}
