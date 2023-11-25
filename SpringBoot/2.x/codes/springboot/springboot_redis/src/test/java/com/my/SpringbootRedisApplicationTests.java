package com.my;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

@Slf4j
@SpringBootTest
class SpringbootRedisApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void set() {
        ValueOperations ops = redisTemplate.opsForValue();
        ops.set("name", "jason");
    }

    @Test
    public void get() {
        ValueOperations ops = redisTemplate.opsForValue();
        Object name = ops.get("name");
        log.info(name.toString());
    }

    @Test
    public void hset() {
        HashOperations ops = redisTemplate.opsForHash();
        ops.put("info", "name", "jason");
    }

    @Test
    public void hget() {
        HashOperations ops = redisTemplate.opsForHash();
        Object name = ops.get("info", "name");
        log.info(name.toString());
    }

}
