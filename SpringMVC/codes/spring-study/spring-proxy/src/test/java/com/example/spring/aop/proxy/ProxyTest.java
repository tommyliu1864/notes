package com.example.spring.aop.proxy;

import org.junit.Test;

public class ProxyTest {


    @Test
    public void testDynamicProxy(){
        ProxyFactory factory = new ProxyFactory(new CalculatorPureImpl());
        Calculator proxy = (Calculator) factory.getProxy();
        proxy.div(4,2);
    }

}
