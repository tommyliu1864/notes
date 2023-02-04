package com.example.mybatis.mapper;

import com.example.mybatis.pojo.Dept;
import org.apache.ibatis.annotations.Param;

public interface DeptMapper {
    Dept getDeptByDeptId(@Param("deptId") Integer deptId);
}
