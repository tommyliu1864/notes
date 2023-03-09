package com.example.ssm.service;

import com.example.ssm.pojo.Employee;
import com.github.pagehelper.PageInfo;

public interface EmployeeService {

    PageInfo<Employee> getEmployeeList(Integer pageNum);

}
