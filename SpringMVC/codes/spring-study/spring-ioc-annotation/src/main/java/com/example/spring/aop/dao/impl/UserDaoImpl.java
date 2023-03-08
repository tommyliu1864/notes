package com.example.spring.aop.dao.impl;

import com.example.spring.aop.pojo.User;
import com.example.spring.aop.dao.UserDao;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {
    @Override
    public void save(User user) {
        System.out.println("UserDaoImpl save user = " + user);
    }
}
