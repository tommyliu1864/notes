package com.example.ssm.service.impl;

import com.example.ssm.mapper.EmployeeMapper;
import com.example.ssm.pojo.Employee;
import com.example.ssm.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public PageInfo<Employee> getEmployeeList(Integer pageNum) {
        PageHelper.startPage(pageNum, 4);
        List<Employee> list = employeeMapper.getEmployeeList();
        PageInfo<Employee> page = new PageInfo<>(list, 5);
        return page;
    }
}
