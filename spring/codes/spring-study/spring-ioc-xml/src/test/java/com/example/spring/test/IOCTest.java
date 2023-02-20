package com.example.spring.test;

import com.example.mybatis.pojo.Student;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class IOCTest {

    @Test
    public void testIOCBySet(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-ioc.xml");
        Student student = applicationContext.getBean("studentG",Student.class);
        System.out.println(student);
    }

    @Test
    public void testIOCByConstructor(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-ioc.xml");
        Student student = applicationContext.getBean("studentB",Student.class);
        System.out.println(student);
    }

    @Test
    public void testDatasource() throws SQLException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-datasource.xml");
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }

}
