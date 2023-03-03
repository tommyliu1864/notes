package com.example.spring.service;

public interface BookService {

    /**
     * 买书，分为三步：
     * 1.查询图书的价格
     * 2.更新图书的库存
     * 3.更新用户的余额
     * @param bookId
     * @param userId
     */
    void buyBook(Integer bookId, Integer userId);

}
