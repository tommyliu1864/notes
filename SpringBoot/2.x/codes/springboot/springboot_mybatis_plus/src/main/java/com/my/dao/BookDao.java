package com.my.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.po.Book;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookDao extends BaseMapper<Book> {
}
