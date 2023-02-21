package com.example.spring.test;

import com.example.spring.pojo.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserFactoryBeanTest {

    @Test
    public void testUserFactoryBean(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-factory.xml");
        User user = (User) applicationContext.getBean("user");
        System.out.println(user);
    }

}
