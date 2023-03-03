package com.example.spring.aop.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

// @Aspect表示这个类是一个切面类
@Aspect
// @Component注解保证这个切面类能够放入IOC容器
@Component
public class LogAspect {

    @Pointcut("execution(* com.example.spring.aop.annotation.CalculatorPureImpl.* (..))")
    public void pointCut(){}

    //@Before("execution(public int com.example.spring.aop.annotation.CalculatorPureImpl.* (..))")
    @Before("pointCut()")
    public void beforeMethod(JoinPoint joinPoint) {
        // 获取连接点的签名信息
        String methodName = joinPoint.getSignature().getName();
        // 获取目标方法到的实参信息
        String args = Arrays.toString(joinPoint.getArgs());
        System.out.println("Logger-->前置通知，方法名:" + methodName + "，参数:" + args);
    }

    @After("execution(public int com.example.spring.aop.annotation.CalculatorPureImpl.* (..))")
    public void afterMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("Logger-->后置通知，方法名:" + methodName);
    }

    @AfterReturning(value = "execution(public int com.example.spring.aop.annotation.CalculatorPureImpl.* (..))", returning = "result")
    public void afterReturningMethod(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("Logger-->返回通知，方法名:" + methodName + "，结果：" + result);
    }

    @AfterThrowing(value = "execution(public int com.example.spring.aop.annotation.CalculatorPureImpl.* (..))", throwing = "ex")
    public void afterThrowingMethod(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("Logger-->返回通知，方法名:" + methodName + "，异常：" + ex);
    }

    @Around("execution(public int com.example.spring.aop.annotation.CalculatorPureImpl.* (..))")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String args = Arrays.toString(joinPoint.getArgs());
        Object result = null;
        try {
            System.out.println("环绕通知-->目标对象方法执行之前");
            //目标方法的执行，目标方法的返回值一定要返回给外界调用者
            result = joinPoint.proceed();
            System.out.println("环绕通知-->目标对象方法返回值之后");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.out.println("环绕通知-->目标对象方法出现异常时");
        } finally {
            System.out.println("环绕通知-->目标对象方法执行完毕");
        }
        return result;
    }
}
