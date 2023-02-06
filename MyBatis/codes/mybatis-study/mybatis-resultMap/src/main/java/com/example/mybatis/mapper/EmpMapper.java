package com.example.mybatis.mapper;

import com.example.mybatis.pojo.Emp;
import org.apache.ibatis.annotations.Param;

public interface EmpMapper {

    Emp getEmpByEmpId(@Param("empId") Integer empId);

    // 获取员工以及所对应的部门信息
    Emp getEmpAndDeptByEmpId(@Param("empId") Integer empId);

    // 根据部门ID查询员工信息
    Emp getEmpByDeptId(@Param("deptId") Integer deptId);
}
