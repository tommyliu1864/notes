package com.my.service;

import com.my.model.po.system.SysLoginLog;
import com.my.security.service.SysLoginLogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SysLoginLogServiceTest {

    @Autowired
    private SysLoginLogService sysLoginLogService;


    @Test
    public void testSave() {
        SysLoginLog sysLoginLog = new SysLoginLog("admin", "'127.0.0.1'", 1, "登录成功");
        sysLoginLogService.save(sysLoginLog);
    }

    @Test
    public void testList() {
        List<SysLoginLog> list = sysLoginLogService.list();
        list.forEach(System.out::println);
    }

}
