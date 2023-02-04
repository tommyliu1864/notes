package com.example.mybatis.test;

import com.example.mybatis.mapper.SelectMapper;
import com.example.mybatis.mapper.UserMapper;
import com.example.mybatis.pojo.User;
import com.example.mybatis.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Map;

public class SelectTest {

    @org.junit.Test
    public void testGetUserById() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        SelectMapper selectMapper = sqlSession.getMapper(SelectMapper.class);
        // 查询一个实体类对象
        User user = selectMapper.getUserById(1);
        System.out.println(user);
        sqlSession.close();
    }

    @org.junit.Test
    public void testGetUserList() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        SelectMapper selectMapper = sqlSession.getMapper(SelectMapper.class);
        // 查询一个list集合
        List<User> users = selectMapper.getUserList();
        users.forEach(System.out::println); // :: 方法引用
        sqlSession.close();
    }

    @org.junit.Test
    public void testGetCount() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        SelectMapper selectMapper = sqlSession.getMapper(SelectMapper.class);
        // 查询单个数据
        int count = selectMapper.getCount();
        System.out.println("count:" + count);
        sqlSession.close();
    }

    @org.junit.Test
    public void testGetUserToMap() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        SelectMapper selectMapper = sqlSession.getMapper(SelectMapper.class);
        // 查询一条数据为map集合
        Map<String, Object> map = selectMapper.getUserToMap(1);
        System.out.println("map:" + map);
        sqlSession.close();
    }

    @org.junit.Test
    public void testGetAllUserToMap() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        SelectMapper selectMapper = sqlSession.getMapper(SelectMapper.class);
        // 查询多条数据为map集合
        // List<Map<String, Object>> users = selectMapper.getAllUserToMap();
        // users.forEach(System.out::println);
        Map<String, Object> map = selectMapper.getAllUserToMap();
        System.out.println(map);
        sqlSession.close();
    }
}
