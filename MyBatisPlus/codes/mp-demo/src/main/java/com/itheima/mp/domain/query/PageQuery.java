package com.itheima.mp.domain.query;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分页查询实体")
public class PageQuery {

    @ApiModelProperty("页码")
    private Integer pageNo;

    @ApiModelProperty("一页的记录数")
    private Integer pageSize;

    @ApiModelProperty("排序字段")
    private String sortBy;

    @ApiModelProperty("是否升序")
    private Boolean isAsc;

    public <T> Page<T> toMpPage(OrderItem... orders) {
        // 1.分页条件
        Page<T> page = Page.of(pageNo, pageSize);
        // 2.排序条件
        // 2.1 先看前端有没有传排序字段
        if (sortBy != null) {
            page.addOrder(new OrderItem(sortBy, isAsc));
            return page;
        }
        // 2.2 再看有没有手动指定排序字段
        if (orders != null) {
            page.addOrder(orders);
        }
        return page;
    }

    public <T> Page<T> toMpPage(String defaultSortBy, boolean isAsc) {
        return this.toMpPage(new OrderItem(defaultSortBy, isAsc));
    }

    public <T> Page<T> toMpPageDefaultSortByCreateTimeDesc() {
        return toMpPage("create_time", false);
    }

    public <T> Page<T> toMpPageDefaultSortByUpdateTimeDesc() {
        return toMpPage("update_time", false);
    }
}
