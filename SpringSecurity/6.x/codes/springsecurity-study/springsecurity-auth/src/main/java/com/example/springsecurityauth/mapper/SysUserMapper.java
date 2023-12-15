package com.example.springsecurityauth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springsecurityauth.pojo.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
