package com.example.spring.aop.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class ValidateAspect {

    @Before("execution(public int com.example.spring.aop.annotation.CalculatorPureImpl.* (..))")
    public void beforeMethod(JoinPoint joinPoint) {
        System.out.println("ValidateAspect 权限校验");
    }

}
