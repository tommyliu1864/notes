package com.hmdp.dto;

import lombok.Data;

import java.util.List;

/**
 * 滚动查询的结果
 */
@Data
public class ScrollResult {
    // 查询到的数据集合
    private List<?> list;
    // 本次查询结果中的最小时间戳，也就是下次查询的最大时间戳
    private Long minTime;
    // 偏移量
    // ZREVRANGEBYSCORE z1 6 0 WITHSCORES LIMIT 2 3
    // 2就是偏移量
    private Integer offset;
}
