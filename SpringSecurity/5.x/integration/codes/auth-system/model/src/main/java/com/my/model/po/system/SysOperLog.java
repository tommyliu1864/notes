package com.my.model.po.system;

import com.my.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 操作日志记录
 */
@ApiModel("操作日志")
@Data
public class SysOperLog extends BaseEntity {

    @ApiModelProperty("模块注释")
    private String module;

    @ApiModelProperty("方法注释")
    private String title;

    @ApiModelProperty("方法名称")
    private String method;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("IP地址")
    private String ipaddr;

    @ApiModelProperty("请求参数")
    private String param;

    @ApiModelProperty("返回结果")
    private String result;

    @ApiModelProperty("执行状态（成功1，失败0）")
    private Integer status;

    @ApiModelProperty("错误消息")
    private String errorMsg;
}
