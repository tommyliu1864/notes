package com.my.model.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("角色查询条件")
@Data
public class SysRoleQueryVO {

    @ApiModelProperty("角色名称")
    private String roleName;

}
