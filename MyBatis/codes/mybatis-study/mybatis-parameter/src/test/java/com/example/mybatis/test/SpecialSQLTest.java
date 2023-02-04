package com.example.mybatis.test;

import com.example.mybatis.mapper.SelectMapper;
import com.example.mybatis.mapper.SpecialSQLMapper;
import com.example.mybatis.pojo.User;
import com.example.mybatis.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class SpecialSQLTest {

    @Test
    public void testGetUserByLike() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        SpecialSQLMapper specialSQLMapper = sqlSession.getMapper(SpecialSQLMapper.class);
        User user = specialSQLMapper.getUserByLike("min");
        System.out.println(user);
        sqlSession.close();
    }

    @Test
    public void testDeleteAll() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        SpecialSQLMapper specialSQLMapper = sqlSession.getMapper(SpecialSQLMapper.class);
        int result = specialSQLMapper.deleteAll("7,8");
        System.out.println("result:" + result);
        sqlSession.close();
    }

    @Test
    public void testGetAllUser() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        SpecialSQLMapper specialSQLMapper = sqlSession.getMapper(SpecialSQLMapper.class);
        // 动态设置表名
        List<User> users = specialSQLMapper.getAllUser("t_user");
        users.forEach(System.out::println);
        sqlSession.close();
    }

    @Test
    public void testInsertUser() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        SpecialSQLMapper specialSQLMapper = sqlSession.getMapper(SpecialSQLMapper.class);
        // 添加功能获取自增的主键
        User user = new User(null, "sa", "123", 20, "男", "123@gmail.com");
        int result = specialSQLMapper.insertUser(user);
        System.out.println("result:" + result);
        System.out.println(user);
        sqlSession.close();
    }
}
