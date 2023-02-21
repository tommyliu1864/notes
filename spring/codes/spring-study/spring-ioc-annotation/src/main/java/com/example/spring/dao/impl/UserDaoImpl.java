package com.example.spring.dao.impl;

import com.example.spring.dao.UserDao;
import com.example.spring.pojo.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {
    @Override
    public void save(User user) {
        System.out.println("UserDaoImpl save user = " + user);
    }
}
