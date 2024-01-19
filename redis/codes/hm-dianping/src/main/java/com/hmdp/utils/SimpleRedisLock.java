package com.hmdp.utils;

import cn.hutool.core.lang.UUID;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 使用redis setnx特性，实现分布式锁
 */
public class SimpleRedisLock implements ILock {

    private static final String KEY_PREFIX = "lock:";
    // 锁的名字
    private String name;
    private StringRedisTemplate stringRedisTemplate;
    // 给线程id的前缀，拼接上uuid前缀，保证不同的机器也不会重复
    private static final String ID_PREFIX = UUID.randomUUID().toString(true) + "-";
    // 释放锁的lua脚本
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;

    static {
        UNLOCK_SCRIPT = new DefaultRedisScript<>();
        UNLOCK_SCRIPT.setLocation(new ClassPathResource("unlock.lua"));
        UNLOCK_SCRIPT.setResultType(Long.class);
    }

    public SimpleRedisLock(String name, StringRedisTemplate stringRedisTemplate) {
        this.name = name;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean tryLock(long timeoutSec) {
        // 获取线程标识
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        // 获取锁
        Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(KEY_PREFIX + name, String.valueOf(threadId), timeoutSec, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(result);
    }

    @Override
    public void unlock() {
        // 获取线程标识
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        /*// 获取锁中的标识
        String id = stringRedisTemplate.opsForValue().get(KEY_PREFIX + name);
        // 判断标识是否一致，防止误删
        if (threadId.equals(id)) {
            // 删除锁
            stringRedisTemplate.delete(KEY_PREFIX + name);
            // 判断完成后，如果删除锁之前，由于GC原因导致导致阻塞时间太长，从而导致锁过期失效，同时有其他线程加锁了，那么这里就会出现误删的情况
        }*/
        // 调用lua脚本
        stringRedisTemplate.execute(
                UNLOCK_SCRIPT,
                Collections.singletonList(KEY_PREFIX + name),
                threadId
        );
    }
}
