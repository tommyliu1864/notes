package com.example.spring.aop.xml;

import org.aspectj.lang.JoinPoint;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class ValidateAspect {

    public void beforeMethod(JoinPoint joinPoint) {
        System.out.println("ValidateAspect 权限校验");
    }

}
