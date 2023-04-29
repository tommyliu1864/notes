package org.example.shiro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.shiro.pojo.User;

public interface UserService extends IService<User> {
    /**
     * 用户登录
     *
     * @param username
     * @return
     */
    User login(String username);
}
