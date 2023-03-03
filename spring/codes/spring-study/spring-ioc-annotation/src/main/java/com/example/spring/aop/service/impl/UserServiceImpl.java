package com.example.spring.aop.service.impl;

import com.example.spring.aop.dao.UserDao;
import com.example.spring.aop.pojo.User;
import com.example.spring.aop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Override
    public void save(User user) {
        System.out.println("UserServiceImpl save user = " + user);
        userDao.save(user);
    }
}
