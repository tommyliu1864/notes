package com.my.model.po.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.my.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("系统用户")
@Data
public class SysUser extends BaseEntity {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("手机")
    private String phone;

    @ApiModelProperty(value = "头像地址")
    private String headUrl;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("状态（1：正常 0：停用）")
    private Integer status;
}
