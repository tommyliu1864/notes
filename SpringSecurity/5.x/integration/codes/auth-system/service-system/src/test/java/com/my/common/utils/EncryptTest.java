package com.my.common.utils;

import cn.hutool.crypto.SecureUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

@SpringBootTest
public class EncryptTest {

    @Test
    public void testEncrypt(){
        String password = SecureUtil.sha1("123456");
        System.out.println(password);
    }

}
