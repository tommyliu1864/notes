package com.example.mybatis.test;

import com.example.mybatis.mapper.DynamicSQLMapper;
import com.example.mybatis.pojo.Emp;
import com.example.mybatis.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DynamicSQLTest {

    @Test
    public void testGetEmpByConditions() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        DynamicSQLMapper dynamicSQLMapper = sqlSession.getMapper(DynamicSQLMapper.class);
        Emp emp = new Emp(null, null, 18, "男", null);
        List<Emp> emps = dynamicSQLMapper.getEmpByConditions(emp);
        System.out.println(emps);
        sqlSession.close();
    }

    @Test
    public void testInsertEmps() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        DynamicSQLMapper dynamicSQLMapper = sqlSession.getMapper(DynamicSQLMapper.class);
        List<Emp> emps = new ArrayList<>();
        emps.add(new Emp(null, "赵四", 18, "男", null));
        emps.add(new Emp(null, "尼古拉斯", 18, "男", null));
        dynamicSQLMapper.insertEmps(emps);
        sqlSession.close();
    }

    @Test
    public void testDeleteEmps() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        DynamicSQLMapper dynamicSQLMapper = sqlSession.getMapper(DynamicSQLMapper.class);
        dynamicSQLMapper.deleteEmps(new int[]{3, 2});
        sqlSession.close();
    }

}


