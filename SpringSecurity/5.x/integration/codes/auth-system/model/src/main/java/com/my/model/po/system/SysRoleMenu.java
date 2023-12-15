package com.my.model.po.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleMenu {

    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("菜单ID")
    private Long menuId;

}
