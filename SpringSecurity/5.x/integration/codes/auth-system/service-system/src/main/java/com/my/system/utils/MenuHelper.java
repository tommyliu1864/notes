package com.my.system.utils;


import com.my.model.po.system.SysMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MenuHelper {

    /**
     * 构建树形结构
     *
     * @param sysMenuList
     * @return
     */
    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {
        //创建集合封装最终数据
        List<SysMenu> trees = new ArrayList<>();
        //遍历所有菜单集合
        for (SysMenu sysMenu : sysMenuList) {
            //找到递归入口，parentid=0
            if (sysMenu.getParentId() == 0) {
                trees.add(findChildren(sysMenu, sysMenuList));
            }
        }
        return trees;
    }

    /**
     * 从根节点进行递归查询，查询子节点
     * 判断 id =  parentid是否相同，如果相同是子节点，进行数据封装
     *
     * @param sysMenu
     * @param treeNodes
     * @return
     */
    private static SysMenu findChildren(SysMenu sysMenu, List<SysMenu> treeNodes) {
        //数据初始化
        sysMenu.setChildren(new ArrayList<>());
        //遍历递归查找
        for (SysMenu it : treeNodes) {
            //比对
            if (Objects.equals(sysMenu.getId(), it.getParentId())) {
                if (sysMenu.getChildren() == null) {
                    sysMenu.setChildren(new ArrayList<>());
                }
                sysMenu.getChildren().add(findChildren(it, treeNodes));
            }
        }
        return sysMenu;
    }
}
