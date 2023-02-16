package com.example.mybatis.test;


import com.example.mybatis.mapper.EmpMapper;
import com.example.mybatis.pojo.Emp;
import com.example.mybatis.pojo.EmpExample;
import com.example.mybatis.utils.SqlSessionUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ReverseTest {

    @Test
    public void testMBG() {
        try {
            InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
            SqlSession sqlSession = sqlSessionFactory.openSession(true);
            EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
            //查询所有数据
            /*List<Emp> list = mapper.selectByExample(null);
            list.forEach(emp -> System.out.println(emp));*/

            //根据条件查询
            /*EmpExample example = new EmpExample();
            example.createCriteria().andEmpNameEqualTo("张 三").andAgeGreaterThanOrEqualTo(20);
            List<Emp> list = mapper.selectByExample(example);
            list.forEach(emp -> System.out.println(emp));*/

            mapper.updateByPrimaryKeySelective(new Emp(1, "admin", 22, null, 3));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPage(){
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        EmpMapper empMapper = sqlSession.getMapper(EmpMapper.class);
        // 查询之前开启分页功能
        PageHelper.startPage(1, 3);
        List<Emp> list = empMapper.selectByExample(null);
        // 获取分页相关数据
        PageInfo<Emp> pageInfo = new PageInfo<>(list, 5);
        System.out.println(pageInfo);
        sqlSession.close();
    }

}
