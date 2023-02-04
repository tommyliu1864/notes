package com.example.mybatis.mapper;

import com.example.mybatis.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SpecialSQLMapper {

    User getUserByLike(@Param("keyword") String keyword);

    int deleteAll(@Param("ids") String ids);

    List<User> getAllUser(@Param("tableName") String tableName);

    int insertUser(User user);
}
