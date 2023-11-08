package com.itheima.mp.service;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.po.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class UserServiceTest {
    @Resource
    private IUserService userService;

    @Test
    void testSaveOneByOne() {
        long b = System.currentTimeMillis();
        for (int i = 1; i <= 1000; i++) {
            userService.save(buildUser(i));
        }
        long e = System.currentTimeMillis();
        System.out.println("耗时：" + (e - b));
    }

    @Test
    void testSaveBatch() {
        List<User> list = new ArrayList<>();
        long b = System.currentTimeMillis();
        for (int i = 1; i <= 1000; i++) {
            list.add(buildUser(i));
            if (i % 100 == 0) {
                userService.saveBatch(list);
                list.clear();
            }
        }
        long e = System.currentTimeMillis();
        System.out.println("耗时：" + (e - b));
    }

    private User buildUser(int i) {
        User user = new User();
        user.setUsername("user_" + i);
        user.setPassword("123");
        user.setPhone("" + (18688190000L + i));
        user.setBalance(2000);
        user.setInfo(new UserInfo(24, "英文老师", "female"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(user.getCreateTime());
        return user;
    }

    @Test
    void testList() {
        List<User> list = userService.list();
        list.forEach(System.out::println);
    }

    @Test
    void testPageQuery() {
        Page<User> page = userService.page(new Page<>(2, 3));
        System.out.println("总条数:" + page.getTotal());
        System.out.println("总页数:" + page.getPages());
        page.addOrder(new OrderItem("balance", false));
        List<User> records = page.getRecords();
        records.forEach(System.out::println);
    }
}
