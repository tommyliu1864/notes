package com.example.mybatis.mapper;

import com.example.mybatis.pojo.Emp;
import org.apache.ibatis.annotations.Param;

public interface CacheMapper {
    Emp getEmpByEmpId(@Param("empId") Integer empId);

    int insertEmp(Emp emp);
}
