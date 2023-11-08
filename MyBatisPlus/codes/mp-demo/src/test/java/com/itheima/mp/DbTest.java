package com.itheima.mp;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DbTest {

    @Test
    void testDBGet() {
        User user = Db.getById(1L, User.class);
        System.out.println(user);
    }

    @Test
    void testDbList() {
        List<User> list = Db.lambdaQuery(User.class)
                .like(User::getUsername, "o")
                .ge(User::getBalance, 1000)
                .list();
        list.forEach(System.out::println);
    }

    @Test
    void testDbUpdate() {
        Db.lambdaUpdate(User.class)
                .set(User::getBalance, 2000)
                .eq(User::getId, 2L);
    }

}
