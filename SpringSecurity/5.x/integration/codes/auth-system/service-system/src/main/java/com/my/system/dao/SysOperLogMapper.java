package com.my.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.model.po.system.SysOperLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysOperLogMapper extends BaseMapper<SysOperLog> {
}
