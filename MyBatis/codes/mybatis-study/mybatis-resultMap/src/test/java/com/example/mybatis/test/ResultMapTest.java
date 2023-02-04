package com.example.mybatis.test;

import com.example.mybatis.mapper.EmpMapper;
import com.example.mybatis.pojo.Emp;
import com.example.mybatis.pojo.User;
import com.example.mybatis.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class ResultMapTest {

    @Test
    public void testGetEmpByEmpId(){
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        EmpMapper selectMapper = sqlSession.getMapper(EmpMapper.class);
        Emp emp = selectMapper.getEmpByEmpId(1);
        System.out.println(emp);
        sqlSession.close();
    }

    @Test
    public void testGetEmpAndDeptByEmpId(){
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        EmpMapper selectMapper = sqlSession.getMapper(EmpMapper.class);
        Emp emp = selectMapper.getEmpAndDeptByEmpId(1);
        System.out.println(emp);
        sqlSession.close();
    }

}


