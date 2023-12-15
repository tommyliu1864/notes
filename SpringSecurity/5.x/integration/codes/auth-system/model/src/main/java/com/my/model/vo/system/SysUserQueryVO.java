package com.my.model.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("系统用户查询条件")
@Data
public class SysUserQueryVO {
    @ApiModelProperty("查询关键词，会根据此关键词匹配用户名和用户姓名")
    private String keyword;

    @ApiModelProperty("创建时间范围，开始时间")
    private String createTimeBegin;

    @ApiModelProperty("创建时间范围，结束时间")
    private String createTimeEnd;
}
