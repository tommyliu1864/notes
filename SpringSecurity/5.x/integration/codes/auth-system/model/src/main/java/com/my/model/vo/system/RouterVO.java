package com.my.model.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 举例说明最终生成的路由信息结构如下：
 * {
 * path: '/system',
 * component: Layout,
 * meta: {
 * title: '系统管理',
 * icon: 'el-icon-s-tools'
 * },
 * alwaysShow: true,
 * children: [
 * {
 * path: 'sysRole',
 * component: () => import('@/views/system/sysRole/list'),
 * meta: {
 * title: '角色管理',
 * icon: 'el-icon-s-custom'
 * }
 * },
 * {
 * path: 'assignAuth',
 * component: () => import('@/views/system/sysRole/assignAuth'),
 * meta: {
 * title: '角色授权'
 * },
 * hidden: true
 * }
 * ]
 * }
 */
@ApiModel("路由信息")
@Data
public class RouterVO {

    @ApiModelProperty("路径")
    private String path;

    @ApiModelProperty("组件")
    private String component;

    @ApiModelProperty("菜单显示信息")
    private MetaVO meta;

    @ApiModelProperty("是否展开显示")
    private Boolean alwaysShow;

    @ApiModelProperty("是否隐藏，当设置true的时候该路由不会再侧边栏出现")
    private Boolean hidden;

    @ApiModelProperty("子节点")
    private List<RouterVO> children;

}
