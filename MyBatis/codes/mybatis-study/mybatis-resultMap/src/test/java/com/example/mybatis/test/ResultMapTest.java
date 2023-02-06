package com.example.mybatis.test;

import com.example.mybatis.mapper.DeptMapper;
import com.example.mybatis.mapper.EmpMapper;
import com.example.mybatis.pojo.Dept;
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
        System.out.println(emp.getEmpName());
        sqlSession.close();
    }

    @Test
    public void testGetDeptByDeptId(){
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        DeptMapper deptMapper = sqlSession.getMapper(DeptMapper.class);
        Dept dept = deptMapper.getDeptByDeptId(1);
        System.out.println(dept);
        sqlSession.close();
    }

}


