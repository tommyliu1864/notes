package com.example.spring.aop.service.impl;

import com.example.spring.aop.dao.UserDao;
import com.example.spring.aop.pojo.User;
import com.example.spring.aop.service.UserService;

public class UserServiceImpl implements UserService {

    private UserDao userDao;

    @Override
    public void save(User user) {
        userDao.save(user);
        System.out.println("UserServiceImpl save user = " + user);
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
