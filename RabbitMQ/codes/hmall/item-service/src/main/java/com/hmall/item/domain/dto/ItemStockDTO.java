package com.hmall.item.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("更新商品库存的信息")
public class ItemStockDTO {

    @ApiModelProperty("商品id")
    private Long id;
    @ApiModelProperty("要退回到仓库的库存数量")
    private Integer stock;

    public ItemStockDTO(Long id, Integer stock) {
        this.id = id;
        this.stock = stock;
    }
}
