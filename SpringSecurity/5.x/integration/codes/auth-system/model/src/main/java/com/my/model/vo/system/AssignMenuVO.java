package com.my.model.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("给角色分配菜单（权限）")
@Data
public class AssignMenuVO {

    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("菜单ID列表")
    private List<Long> menusIds;
}
