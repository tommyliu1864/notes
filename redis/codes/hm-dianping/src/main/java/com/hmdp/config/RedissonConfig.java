package com.hmdp.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedissonClient redissonClient() {
        // 配置
        Config config = new Config();
        config.useSingleServer()
                .setAddress(String.format("redis://%s:%s", host, port))
                .setPassword(password);
        // 创建redisson client对象
        return Redisson.create(config);
    }

    @Bean
    public RedissonClient redissonClient2() {
        // 配置
        Config config = new Config();
        config.useSingleServer()
                .setAddress(String.format("redis://%s:%s", host, "6380"))
                .setPassword(password);
        // 创建redisson client对象
        return Redisson.create(config);
    }

    @Bean
    public RedissonClient redissonClient3() {
        // 配置
        Config config = new Config();
        config.useSingleServer()
                .setAddress(String.format("redis://%s:%s", host, "6381"))
                .setPassword(password);
        // 创建redisson client对象
        return Redisson.create(config);
    }

}
