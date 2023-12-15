package com.my.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.model.po.system.SysMenu;
import com.my.model.po.system.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户ID查询所分配的角色名称列表
     * @param userId
     * @return
     */
    @Select("select r.role_name from sys_role r " +
            "left join sys_user_role ur on r.id = ur.role_id and ur.deleted = 0 " +
            "where ur.user_id = #{userId} and r.deleted = 0")
    List<String> getRoleNames(Long userId);

    /**
     * 根据用户ID查询用户所具有的权限，包括（菜单（包含隐藏菜单）和按钮级别的所有权限）
     * @param userId
     * @return
     */
    @Select("select m.* from sys_menu m " +
            "inner join sys_role_menu rm on m.id = rm.menu_id and m.deleted = 0 and rm.deleted = 0 and m.status = 1 " +
            "inner join sys_user_role ur on rm.role_id = ur.role_id and ur.deleted = 0 " +
            "where ur.user_id = #{userId}")
    List<SysMenu> getAuthorities(Long userId);

}
