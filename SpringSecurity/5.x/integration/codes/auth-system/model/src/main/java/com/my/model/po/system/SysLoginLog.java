package com.my.model.po.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.my.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;

/**
 * 用户登录日志记录
 */
@ApiModel("登录日志")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysLoginLog extends BaseEntity {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("登录IP地址")
    @TableField("ipaddr")
    private String ipaddr;

    @ApiModelProperty("登录状态（1成功，0失败）")
    private Integer status;

    @ApiModelProperty("提示信息")
    private String msg;

}
