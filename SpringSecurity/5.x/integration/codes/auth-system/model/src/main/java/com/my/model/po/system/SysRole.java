package com.my.model.po.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.my.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("角色信息")
@Data
public class SysRole extends BaseEntity {

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色编号")
    private String roleCode;

    @ApiModelProperty("角色描述")
    private String description;

}
