package com.example.spring.aop;

import com.example.spring.aop.annotation.Calculator;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AOPTest {

    @Test
    public void testAOPByAnnotation() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-aop.xml");
        Calculator calculator = applicationContext.getBean(Calculator.class);
        calculator.div(1, 2);
    }


}
