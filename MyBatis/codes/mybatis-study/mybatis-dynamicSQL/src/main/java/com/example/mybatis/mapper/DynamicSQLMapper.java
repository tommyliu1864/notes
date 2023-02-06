package com.example.mybatis.mapper;

import com.example.mybatis.pojo.Emp;

import java.util.List;

public interface DynamicSQLMapper {
    // 根据条件查询员工信息
    List<Emp> getEmpByConditions(Emp emp);
}
