package com.example.mybatis.test;

import com.example.mybatis.mapper.DynamicSQLMapper;
import com.example.mybatis.pojo.Emp;
import com.example.mybatis.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

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
}


