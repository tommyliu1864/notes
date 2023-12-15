package com.my.service;

import com.my.model.po.system.SysRole;
import com.my.system.service.SysRoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SysRoleServiceTest {

    @Autowired
    private SysRoleService sysRoleService;

    @Test
    public void testGetAll() {
        List<SysRole> list = sysRoleService.list();
        list.forEach(System.out::println);
    }

    @Test
    public void testNothing() {
        String a = "abcefsadfds".substring(0, 5);
        System.out.println(a);
    }

}
