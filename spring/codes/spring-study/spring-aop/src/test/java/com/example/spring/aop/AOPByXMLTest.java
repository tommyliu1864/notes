package com.example.spring.aop;

import com.example.spring.aop.xml.Calculator;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AOPByXMLTest {

    @Test
    public void testAOPByXML() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-xml-aop.xml");
        Calculator calculator = applicationContext.getBean(Calculator.class);
        calculator.div(1, 2);
    }

}
