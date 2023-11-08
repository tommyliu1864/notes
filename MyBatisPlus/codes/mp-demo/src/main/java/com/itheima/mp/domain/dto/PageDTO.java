package com.itheima.mp.domain.dto;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.UserVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@ApiModel(description = "分页结果")
@AllArgsConstructor
public class PageDTO<T> {

    @ApiModelProperty("总条数")
    private Long total;

    @ApiModelProperty("总页数")
    private Long pages;

    @ApiModelProperty("数据集合")
    private List<T> list;

    /**
     * 返回空分页结果
     *
     * @param page MyBatisPlus的分页结果
     * @param <V>  目标VO类型
     * @param <P>  原始PO类型
     * @return VO的分页对象
     */
    public static <V, P> PageDTO<V> empty(Page<P> page) {
        return new PageDTO<>(page.getTotal(), page.getPages(), Collections.emptyList());
    }

    /**
     * 返回空分页结果
     *
     * @param page    page MyBatisPlus的分页结果
     * @param voClass 目标VO类型的字节码
     * @param <V>     目标VO类型
     * @param <P>     原始PO类型
     * @return VO的分页对象
     */
    public static <V, P> PageDTO<V> of(Page<P> page, Class<V> voClass) {
        // 1. 数据非空校验
        List<P> records = page.getRecords();
        if (records == null || records.size() == 0) {
            // 无数据，返回空结果
            return empty(page);
        }
        // 2.有数据，转换
        List<V> list = BeanUtil.copyToList(records, voClass);
        // 3.封装返回
        return new PageDTO<V>(page.getTotal(), page.getPages(), list);
    }

    /**
     * 返回空分页结果
     * @param page page MyBatisPlus的分页结果
     * @param convertor PO到VO的转换函数
     * @return VO的分页对象
     * @param <V> 目标VO类型
     * @param <P> 原始PO类型
     */

    public static <V, P> PageDTO<V> of(Page<P> page, Function<P, V> convertor) {
        // 1. 数据非空校验
        List<P> records = page.getRecords();
        if (records == null || records.size() == 0) {
            // 无数据，返回空结果
            return empty(page);
        }
        // 2.有数据，转换
        List<V> list = records.stream().map(convertor).collect(Collectors.toList());
        // 3.封装返回
        return new PageDTO<>(page.getTotal(), page.getPages(), list);
    }
}
