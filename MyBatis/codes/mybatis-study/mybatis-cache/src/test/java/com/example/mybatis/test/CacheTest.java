package com.example.mybatis.test;

import com.example.mybatis.mapper.CacheMapper;
import com.example.mybatis.pojo.Emp;
import com.example.mybatis.utils.SqlSessionUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CacheTest {

    @Test
    public void testFirstLeveCache1() {
        // 缓存命中
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        CacheMapper cacheMapper = sqlSession.getMapper(CacheMapper.class);
        Emp emp = cacheMapper.getEmpByEmpId(4);
        System.out.println(emp.toString());
        Emp emp2 = cacheMapper.getEmpByEmpId(4);
        System.out.println(emp2.toString());
        sqlSession.close();
    }

    @Test
    public void testFirstLeveCache2() {
        // 不同的SqlSession对应不同的一级缓存
        SqlSession sqlSession1 = SqlSessionUtil.getSqlSession();
        CacheMapper cacheMapper1 = sqlSession1.getMapper(CacheMapper.class);
        Emp emp = cacheMapper1.getEmpByEmpId(4);
        System.out.println(emp.toString());

        SqlSession sqlSession2 = SqlSessionUtil.getSqlSession();
        CacheMapper cacheMapper2 = sqlSession2.getMapper(CacheMapper.class);
        Emp emp2 = cacheMapper2.getEmpByEmpId(4);
        System.out.println(emp2.toString());
        sqlSession1.close();
        sqlSession2.close();
    }

    @Test
    public void testFirstLeveCache3() {
        // 同一个SqlSession但是查询条件不同
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        CacheMapper cacheMapper = sqlSession.getMapper(CacheMapper.class);
        Emp emp = cacheMapper.getEmpByEmpId(4);
        System.out.println(emp.toString());
        Emp emp2 = cacheMapper.getEmpByEmpId(5);
        System.out.println(emp2.toString());
        sqlSession.close();
    }

    @Test
    public void testFirstLeveCache4() {
        // 同一个SqlSession两次查询期间执行了任何一次增删改操作
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        CacheMapper cacheMapper = sqlSession.getMapper(CacheMapper.class);
        Emp emp = cacheMapper.getEmpByEmpId(4);
        System.out.println(emp.toString());
        // 添加一行数据
        cacheMapper.insertEmp(new Emp(null, "Jack", 19, "男", null));
        Emp emp2 = cacheMapper.getEmpByEmpId(4);
        System.out.println(emp2.toString());
        sqlSession.close();
    }

    @Test
    public void testFirstLeveCache5() {
        // 同一个SqlSession两次查询期间手动清空了缓存
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        CacheMapper cacheMapper = sqlSession.getMapper(CacheMapper.class);
        Emp emp = cacheMapper.getEmpByEmpId(4);
        System.out.println(emp.toString());
        // 清空缓存
        sqlSession.clearCache();
        Emp emp2 = cacheMapper.getEmpByEmpId(4);
        System.out.println(emp2.toString());
        sqlSession.close();
    }

    @Test
    public void testSecondLevelCache() throws Exception {
        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);

        SqlSession sqlSession1 = sqlSessionFactory.openSession(true);
        CacheMapper cacheMapper1 = sqlSession1.getMapper(CacheMapper.class);
        Emp emp1 = cacheMapper1.getEmpByEmpId(4);
        System.out.println(emp1);
        // 二级缓存必须在SqlSession关闭或提交之后有效
        sqlSession1.close();

        SqlSession sqlSession2 = sqlSessionFactory.openSession(true);
        CacheMapper cacheMapper2 = sqlSession2.getMapper(CacheMapper.class);
        Emp emp2 = cacheMapper2.getEmpByEmpId(4);
        System.out.println(emp2);
        sqlSession2.close();
    }
}


