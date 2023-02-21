package com.example.spring.service.impl;

import com.example.spring.dao.UserDao;
import com.example.spring.pojo.User;
import com.example.spring.service.UserService;

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
