package com.my.model.vo.system;

import com.my.model.po.system.SysRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("用户对应的角色列表")
@Data
public class SysUserRoleResultVO {

    @ApiModelProperty("系统中的所有角色列表")
    private List<SysRole> allRoles;

    @ApiModelProperty("用户已经具备的角色ID的列表")
    private List<Long> userRoleIds;
}
