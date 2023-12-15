package com.my.mapper;

import com.my.system.dao.SysUserMapper;
import com.my.model.po.system.SysMenu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SysUserMapperTest {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Test
    public void testGetRoleNames() {
        List<String> roleNames = sysUserMapper.getRoleNames(3L);
        System.out.println(roleNames);
    }


    @Test
    public void testGetAuthorities(){
        List<SysMenu> authorities = sysUserMapper.getAuthorities(3L);
        System.out.println(authorities);
    }

}
