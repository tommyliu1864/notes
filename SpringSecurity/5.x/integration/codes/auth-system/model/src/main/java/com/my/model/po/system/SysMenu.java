package com.my.model.po.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.my.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@ApiModel("菜单")
@Data
public class SysMenu extends BaseEntity {

    @ApiModelProperty("所属上级")
    private Long parentId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类型，0：目录，1：菜单，2：按钮")
    private Integer type;

    @ApiModelProperty("当type为1时，菜单对应前端路由的path")
    private String path;

    @ApiModelProperty("当type为1时，菜单对应前端路由的component")
    private String component;

    @ApiModelProperty("当type为2时，按钮的功能权限标示")
    private String perms;

    @ApiModelProperty("当type为1时，菜单的图标")
    private String icon;

    @ApiModelProperty("排序")
    private Integer sortValue;

    @ApiModelProperty("状态，0：禁止，1：正常")
    private Integer status;

    @ApiModelProperty("下级列表")
    @TableField(exist = false)
    private List<SysMenu> children;

    @ApiModelProperty("是否已经授权")
    @TableField(exist = false)
    private boolean isSelect;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SysMenu sysMenu = (SysMenu) o;
        return Objects.equals(getId(), sysMenu.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId());
    }
}
