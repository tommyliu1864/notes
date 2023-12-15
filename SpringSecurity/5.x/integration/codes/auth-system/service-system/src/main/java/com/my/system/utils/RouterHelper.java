package com.my.system.utils;

import com.my.model.po.system.SysMenu;
import com.my.model.vo.system.MetaVO;
import com.my.model.vo.system.RouterVO;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class RouterHelper {

    /**
     * 根据菜单构建路由
     *
     * @param menus
     * @return
     */
    public static List<RouterVO> buildRouters(List<SysMenu> menus) {
        List<RouterVO> routers = new LinkedList<>();
        for (SysMenu menu : menus) {
            RouterVO router = createRouter(menu, false); // 先创建一个路由出来
            List<SysMenu> children = menu.getChildren();
            switch (menu.getType()){
                case 0:
                    routers.add(router);
                    // 如果还有子菜单，则进入递归
                    if (!CollectionUtils.isEmpty(children)) {
                        router.setAlwaysShow(true); // 展开
                        router.setChildren(buildRouters(children));
                    }
                    break;
                case 1:
                    routers.add(router);
                    // type为1，下面会包含按钮或者需要隐藏显示的子菜单（路由）
                    // 需要隐藏的子菜单，如“系统管理”下面的“角色授权”
                    if (!CollectionUtils.isEmpty(children)){
                        children.forEach(it -> {
                            // 通过component是否有值来找到这些需要隐藏的路由
                            if (StringUtils.hasText(it.getComponent())){
                                routers.add(createRouter(it, true));
                            }
                        });
                    }
                    break;
            }
        }
        return routers;
    }

    /**
     * 根据SysMenu对象构建出来
     * @param menu
     * @param hidden
     * @return
     */
    private static RouterVO createRouter(SysMenu menu, boolean hidden) {
        RouterVO router = new RouterVO();
        router.setHidden(hidden);
        router.setAlwaysShow(false);
        router.setPath(getRouterPath(menu));
        router.setComponent(menu.getComponent());
        router.setMeta(new MetaVO(menu.getName(), menu.getIcon()));
        return router;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    private static String getRouterPath(SysMenu menu) {
        // 当parentId为0，拼接上 "/"
        return menu.getParentId() == 0 ? "/" + menu.getPath() : menu.getPath();
    }

}
