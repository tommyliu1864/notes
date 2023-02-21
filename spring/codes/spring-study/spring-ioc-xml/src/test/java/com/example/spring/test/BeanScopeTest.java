package com.example.spring.test;

import com.example.spring.pojo.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanScopeTest {

    @Test
    public void testBeanScope() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-scope.xml");
        User user1 = applicationContext.getBean(User.class);
        User user2 = applicationContext.getBean(User.class);
        System.out.println(user1 == user2);
    }

    @Test
    public void testBeanLifeCycle() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-scope.xml");
        User user1 = applicationContext.getBean(User.class);
        User user2 = applicationContext.getBean(User.class);
        System.out.println(user1 == user2);
        System.out.println("生命周期:4、通过IOC容器获取bean并使用");
        applicationContext.close();
    }

}
