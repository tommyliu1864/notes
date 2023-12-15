package com.my.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.system.dao.SysMenuMapper;
import com.my.system.dao.SysRoleMapper;
import com.my.system.dao.SysUserMapper;
import com.my.system.dao.SysUserRoleMapper;
import com.my.model.po.system.SysMenu;
import com.my.model.po.system.SysRole;
import com.my.model.po.system.SysUser;
import com.my.model.po.system.SysUserRole;
import com.my.model.vo.system.AssignRoleVO;
import com.my.model.vo.system.RouterVO;
import com.my.model.vo.system.SysUserAuthVO;
import com.my.model.vo.system.SysUserRoleResultVO;
import com.my.system.service.SysUserService;
import com.my.system.utils.MenuHelper;
import com.my.system.utils.RouterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public SysUserRoleResultVO getRoles(Long userId) {
        SysUserRoleResultVO resultVO = new SysUserRoleResultVO();
        // 所有的角色列表
        List<SysRole> allRoles = sysRoleMapper.selectList(null);
        resultVO.setAllRoles(allRoles);
        // 查询用户的角色列表
        LambdaQueryWrapper<SysUserRole> wrapper = new QueryWrapper<SysUserRole>()
                .lambda()
                .eq(SysUserRole::getUserId, userId);
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(wrapper);
        // 提取List<SysUserRole>中的所有roleId
        List<Long> userRoleIds = sysUserRoles.stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());
        resultVO.setUserRoleIds(userRoleIds);
        return resultVO;
    }

    @Transactional
    @Override
    public void assignRoles(AssignRoleVO vo) {
        // 1.先删除用户原来的角色信息
        LambdaQueryWrapper<SysUserRole> wrapper = new QueryWrapper<SysUserRole>()
                .lambda().eq(SysUserRole::getUserId, vo.getUserId());
        sysUserRoleMapper.delete(wrapper);
        // 2.给用户重新添加角色信息
        List<Long> roleIds = vo.getRoleIds();
        if (roleIds != null) {
            roleIds.forEach((roleId) -> {
                SysUserRole sysUserRole = new SysUserRole(vo.getUserId(), roleId);
                sysUserRoleMapper.insert(sysUserRole);
            });
        }
    }

    @Override
    public SysUserAuthVO getSysUserAuthInfo(String username) {
        SysUserAuthVO result = new SysUserAuthVO();
        // 根据用户名查询用户信息
        LambdaQueryWrapper<SysUser> sysUserWrapper = new QueryWrapper<SysUser>().lambda()
                .eq(SysUser::getStatus, 1) // 可用状态
                .eq(SysUser::getUsername, username);
        SysUser sysUser = getBaseMapper().selectOne(sysUserWrapper);
        result.setUsername(username);
        result.setAvatar(sysUser.getHeadUrl());
        Long userId = sysUser.getId();
        // 1.角色列表
        List<String> roleNames = getBaseMapper().getRoleNames(userId);
        result.setRoles(roleNames);
        // 2.菜单（路由）信息
        List<RouterVO> routers = getRouters(userId);
        result.setRouters(routers);
        // 3.按钮信息
        List<String> buttons = getButtons(userId);
        result.setButtons(buttons);
        return result;
    }

    /**
     * 获取用户具备的权限信息（只包含按钮级别）
     *
     * @param userId
     * @return
     */
    private List<String> getButtons(Long userId) {
        List<SysMenu> sysMenus;
        // 如果用户ID为1，为超级管理员，则应该具备所有权限
        if (userId == 1) {
            LambdaQueryWrapper<SysMenu> sysMenuWrapper = new QueryWrapper<SysMenu>().lambda()
                    .eq(SysMenu::getStatus, 1)  // 可用状态
                    .eq(SysMenu::getType, 2)  // 类型为2
                    .orderByAsc(SysMenu::getSortValue);
            sysMenus = sysMenuMapper.selectList(sysMenuWrapper);
        } else {
            // 用户不是超级管理员，则查询他的相关权限
            List<SysMenu> buttons = getBaseMapper().getAuthorities(userId);
            // 过滤掉那些隐藏菜单，按钮级别的权限component为null
            sysMenus = buttons.stream()
                    .filter(it -> it.getType() == 2 && !StringUtils.hasText(it.getComponent()))
                    .collect(Collectors.toList());

        }
        // SysMenu集合转为String集合
        return sysMenus.stream()
                .map(SysMenu::getPerms)
                .collect(Collectors.toList());
    }

    /**
     * 获取路由信息
     *
     * @param userId
     * @return
     */
    private List<RouterVO> getRouters(Long userId) {
        List<SysMenu> sysMenus;
        // 如果用户ID为1，为超级管理员，则应该具备所有权限
        if (userId == 1) {
            LambdaQueryWrapper<SysMenu> sysMenuWrapper = new QueryWrapper<SysMenu>().lambda()
                    .eq(SysMenu::getStatus, 1)
                    .orderByAsc(SysMenu::getSortValue);
            sysMenus = sysMenuMapper.selectList(sysMenuWrapper);
        } else {
            // 其他用户，则需要另外查询
            sysMenus = getBaseMapper().getAuthorities(userId)
                    .stream().distinct().collect(Collectors.toList()); //去重处理
        }
        // 转成树形结构
        sysMenus = MenuHelper.buildTree(sysMenus);
        // 构建出路由列表
        return RouterHelper.buildRouters(sysMenus);
    }


}
