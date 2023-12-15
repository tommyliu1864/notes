package com.my.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.model.po.system.SysMenu;
import com.my.model.po.system.SysRole;
import com.my.model.vo.system.AssignMenuVO;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

    /**
     *
     * 给角色授权之前，需查询获取所有的菜单列表，那些已经被授权的菜单，将被选中
     * @param roleId
     * @return
     */
    List<SysMenu> getMenus(Long roleId);

    /**
     *
     * 给角色分配菜单（权限）
     * @param vo
     */
    void assignMenus(AssignMenuVO vo);
}
