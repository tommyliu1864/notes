package com.my.model.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class BaseEntity implements Serializable {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @JsonIgnore
    @ApiModelProperty("逻辑删除，0 代表未删除，1 代表已删除")
    private Integer deleted;

}
