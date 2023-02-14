package com.example.mybatis.mapper;

import com.example.mybatis.pojo.Emp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DynamicSQLMapper {
    // 根据条件查询员工信息
    List<Emp> getEmpByConditions(Emp emp);

    // 批量添加员工
    int insertEmps(@Param("emps") List<Emp> emps);

    // 批量删除员工
    int deleteEmps(@Param("ids") int[] ids);
}
