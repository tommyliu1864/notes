package com.hmdp;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
public class TokenGenerateTest {

    private final String fileName = "D://tokens.txt";

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private IUserService userService;

    // 生成一个tokens.txt文件，里面有1000个token
    @Test
    void generateTokens() throws IOException {
        FileWriter writer = new FileWriter(fileName);
        for (int i = 0; i < 1000; i++) {
            String token = UUID.randomUUID().toString(true);
            writer.write(token);
            writer.write("\n");
        }
        writer.flush();
        writer.close();
    }

    // 把1000个token从文件读取出来，添加到redis缓存中
    @Test
    void addRedisToken() throws IOException {
        // 1.先查询出所有用户，从里面取1000个
        List<User> users = userService.list();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = null;
        int index = 0;
        while ((line = reader.readLine()) != null) {
            User user = users.get(index++);
            // 将User对象转为HashMap存储
            UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
            Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                    CopyOptions.create()
                            .setIgnoreNullValue(true)
                            // hash只能存string类型，但是id是Long类型，所以这里需要把类型转为String
                            .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
            // 2.把token和用户信息存入redis
            String token = line.trim();
            String tokenKey = RedisConstants.LOGIN_USER_KEY + token;
            stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
            // 设置token有效期
            stringRedisTemplate.expire(tokenKey, 100, TimeUnit.DAYS);
            log.info("index: {}, token:{}", index, token);
        }
    }

}
