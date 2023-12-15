package com.my.model.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("菜单信息")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetaVO {

    @ApiModelProperty("菜单的标题")
    private String title;
    @ApiModelProperty("菜单的图标")
    private String icon;

}
