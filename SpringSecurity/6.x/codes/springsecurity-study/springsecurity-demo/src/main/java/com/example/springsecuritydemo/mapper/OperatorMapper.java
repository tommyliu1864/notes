package com.example.springsecuritydemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springsecuritydemo.model.po.Operator;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperatorMapper extends BaseMapper<Operator> {


}
