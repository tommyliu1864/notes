package com.my.security.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.model.po.system.SysLoginLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {
}
