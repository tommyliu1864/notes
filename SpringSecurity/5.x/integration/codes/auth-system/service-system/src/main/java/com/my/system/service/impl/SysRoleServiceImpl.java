package com.my.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.system.dao.SysMenuMapper;
import com.my.system.dao.SysRoleMapper;
import com.my.system.dao.SysRoleMenuMapper;
import com.my.model.po.system.SysMenu;
import com.my.model.po.system.SysRole;
import com.my.model.po.system.SysRoleMenu;
import com.my.model.vo.system.AssignMenuVO;
import com.my.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysMenu> getMenus(Long roleId) {
        // 1.查询所有的菜单列表
        LambdaQueryWrapper<SysMenu> sysMenuWrapper = new QueryWrapper<SysMenu>().lambda()
                .eq(SysMenu::getStatus, 1);
        List<SysMenu> result = sysMenuMapper.selectList(sysMenuWrapper);
        // 2.获取已经授权的菜单列表
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuWrapper = new QueryWrapper<SysRoleMenu>().lambda()
                .eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectList(sysRoleMenuWrapper);
        List<Long> selectedMenuIds = sysRoleMenus.stream()
                .map(SysRoleMenu::getMenuId)
                .collect(Collectors.toList());
        // 3.将已授权的菜单状态设置为选中状态
        result.forEach(it -> {
            if (selectedMenuIds.contains(it.getId())) {
                it.setSelect(true);
            }
        });
        return result;
    }

    @Override
    public void assignMenus(AssignMenuVO vo) {
        // 1.删除原有的权限
        LambdaQueryWrapper<SysRoleMenu> deleteWrapper = new QueryWrapper<SysRoleMenu>().lambda()
                .eq(SysRoleMenu::getRoleId, vo.getRoleId());
        sysRoleMenuMapper.delete(deleteWrapper);
        // 2.保存新的权限记录
        List<Long> menusIds = vo.getMenusIds();
        if (menusIds != null) {
            menusIds.forEach(it -> {
                sysRoleMenuMapper.insert(new SysRoleMenu(vo.getRoleId(), it));
            });
        }
    }
}
