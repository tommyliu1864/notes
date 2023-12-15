package com.my.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.model.po.system.SysUser;
import com.my.model.vo.system.AssignRoleVO;
import com.my.model.vo.system.SysUserAuthVO;
import com.my.model.vo.system.SysUserRoleResultVO;

import java.util.List;
import java.util.Map;

public interface SysUserService extends IService<SysUser> {

    /**
     * 根据用户ID获取用户角色信息，包括：
     * 1.所有的角色列表
     * 2.userId所具有的角色列表
     *
     * @param userId
     * @return
     */
    SysUserRoleResultVO getRoles(Long userId);

    /**
     * 给用户分配角色
     * @param vo
     */
    void assignRoles(AssignRoleVO vo);

    /**
     * 获取系统用户信息，包含权限相关信息
     * @param username
     * @return
     */
    SysUserAuthVO getSysUserAuthInfo(String username);
}
