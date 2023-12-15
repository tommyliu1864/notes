package com.my.model.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("登录日志查询条件")
@Data
public class SysLoginLogQueryVO {
    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("访问时间范围，开始时间")
    private String createTimeBegin;

    @ApiModelProperty("访问时间范围，结束时间")
    private String createTimeEnd;
}
