package com.example.springsecurityjwt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springsecurityjwt.model.po.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 查询指定用户下的所有权限集合
     * @param userId
     * @return
     */
    @Select("SELECT permission_id FROM sys_role_permission WHERE role_id in (SELECT role_id FROM sys_user_role WHERE user_id = #{userId})")
    Set<Integer> getAllPermissions(Integer userId);

    /**
     * 查询指定用户的所有角色集合
     * @param userId
     * @return
     */
    @Select("SELECT role_id FROM sys_user_role WHERE user_id = #{userId}")
    Set<Integer> getAllRoles(Integer userId);
}
