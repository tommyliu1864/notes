package com.my.model.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("给用户分配角色")
@Data
public class AssignRoleVO {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("角色列表ID")
    private List<Long> roleIds;

}
