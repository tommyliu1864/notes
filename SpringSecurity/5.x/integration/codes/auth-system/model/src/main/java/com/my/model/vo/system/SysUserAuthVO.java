package com.my.model.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("系统用户信息")
@Data
public class SysUserAuthVO {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("用户角色列表")
    private List<String> roles;

    @ApiModelProperty("路由列表，显示在左侧菜单栏中")
    private List<RouterVO> routers;

    @ApiModelProperty("按钮权限列表")
    private List<String> buttons;
}
