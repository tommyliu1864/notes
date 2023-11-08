package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.mp.domain.po.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    User queryUserById(Long id);

    @Select("UPDATE user set balance = balance - #{money} ${ew.customSqlSegment}")
    void deductBalanceByIds(@Param("money") int money, @Param("ew") QueryWrapper<User> wrapper);

    @Select("SELECT u.* FROM user u inner join address a ON u.id = a.user_id ${ew.customSqlSegment}")
    List<User> queryUserByWrapper(@Param("ew") QueryWrapper<User> wrapper);

    List<User> queryUserByIdAndAddr(@Param("ew") QueryWrapper<User> wrapper);

    @Update("UPDATE user SET balance = balance - #{money} WHERE id = #{id}")
    void deductMoneyById(@Param("id") Long id, @Param("money") Integer money);
}
