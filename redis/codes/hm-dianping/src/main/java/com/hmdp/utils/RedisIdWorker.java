package com.hmdp.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 基于redis的全局ID生成器，确保不会重复ID
 */
@Component
public class RedisIdWorker {

    // 开始的时间戳(2022年1月1日距离1970年1月1日过去了多少秒)
    private static final long BEGIN_TIMESTAMP = 1640995200L;
    // 序列号的位数
    private static final int COUNT_BITS = 32;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public long nextId(String keyPrefix){
        // 1.生成时间戳（当前时间距离BEGIN_TIMESTAMP，过去了多少秒）
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        // 这里之所以减去一个时间戳的目的是为了让timestamp的值不能太大，这样它的高位就可以全部为0，为后面的左移做准备
        long timestamp = nowSecond - BEGIN_TIMESTAMP;
        // 2.生成序列号
        // 通过redis的自增长，生成序列号
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd")); // 一天使用一个key
        long count = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date);
        // 3.拼接并返回
        // 一个long类型64位，左移让出低位32位（全部为0），异或运算拼接上序列号
        return timestamp << COUNT_BITS | count;
    }

    /*public static void main(String[] args) {
        // BEGIN_TIMESTAMP计算方法
        LocalDateTime begin = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
        System.out.println(begin.toEpochSecond(ZoneOffset.UTC));
    }*/

}
