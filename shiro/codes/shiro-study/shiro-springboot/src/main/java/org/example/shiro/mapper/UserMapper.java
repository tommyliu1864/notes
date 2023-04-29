package org.example.shiro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.shiro.pojo.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
