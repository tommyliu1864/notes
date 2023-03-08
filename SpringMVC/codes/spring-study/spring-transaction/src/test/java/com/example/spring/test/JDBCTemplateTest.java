package com.example.spring.test;

import com.example.spring.pojo.Emp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-jdbc.xml")
public class JDBCTemplateTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testInsert() {
        //测试增删改功能
        String sql = " insert into t_emp values(null, ?, ?, ?, null) ";
        int result = jdbcTemplate.update(sql, "张三", 23, "男");
        System.out.println(result);
    }

    @Test
    public void testSelectEmpById() {
        //查询一条数据为一个实体类对象
        String sql = " select * from t_emp where emp_id = ? ";
        Emp emp = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Emp.class), 4);
        System.out.println(emp);
    }

    @Test
    public void testSelectList() {
        //查询多条数据为一个list集合
        String sql = " select * from t_emp ";
        List<Emp> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Emp.class));
        list.forEach(emp -> System.out.println(emp));
    }

    @Test
    public void selectCount() {
        // 查询单行单列的值
        String sql = " select count(emp_id) from t_emp ";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        System.out.println(count);
    }
}
