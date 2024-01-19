package com.hmdp.utils;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hmdp.entity.Shop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
@Component
public class CacheClient {
    private final StringRedisTemplate stringRedisTemplate;

    // 用户逻辑过期方案下，使用子线程重建缓存
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    public CacheClient(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 将对象以json字符串方式存入redis，并设置过期时间
     *
     * @param key
     * @param value
     * @param time
     * @param unit
     */
    public void set(String key, Object value, Long time, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), time, unit);
    }

    /**
     * 逻辑过期，redis并不会到期删除数据，而是程序代码会根据过期时间重建缓存
     *
     * @param key
     * @param value
     * @param time
     * @param unit
     */
    public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit unit) {
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    /**
     * 防止缓存穿透的解决方案
     *
     * @param keyPrefix
     * @param id         需要获取的对象的id
     * @param type       对象的类型
     * @param dbFallback 如果缓存中没有该对象，需要从数据库中查询
     * @param time
     * @param unit
     * @param <R>        返回值泛型
     * @param <ID>       ID的泛型
     * @return
     */
    public <R, ID> R queryWithPassThrough(
            String keyPrefix,
            ID id,
            Class<R> type,
            Function<ID, R> dbFallback,
            Long time, TimeUnit unit) {
        // 1.从redis查询商铺缓存
        String key = keyPrefix + id;
        String shopJson = stringRedisTemplate.opsForValue().get(key);
        // 2.判断是否存在
        if (StrUtil.isNotBlank(shopJson)) {
            // 3.存在，直接返回
            return JSONUtil.toBean(shopJson, type);
        }
        // 修改处：是空值，但是不为null，直接返回，防止缓存穿透
        if (shopJson != null) {
            return null;
        }
        // 4.不存在，根据id查询数据库
        R r = dbFallback.apply(id);
        // 5.数据库中不存在，返回错误
        if (r == null) {
            // 修改处：防止缓存穿透，将空值写入redis，并设置有效期为2分钟
            stringRedisTemplate.opsForValue().set(key, "", RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
            return null;
        }
        // 6.存在，写入redis
        this.set(key, r, time, unit);
        // 7.返回
        return r;
    }

    /**
     * 互斥锁方案解决缓存击穿问题
     *
     * @param keyPrefix 对象存入redis的前缀，完整的key为 keyPrefix + id
     * @param lockKeyPrefix，对象互斥锁存入redis的前缀，完整的key为 lockKeyPrefix + id
     * @param id 需要获取的对象的id
     * @param type 对象的类型
     * @param dbFallback 如果缓存中没有该对象，需要从数据库中查询
     * @param time keyPrefix对象存入redis的有效时间
     * @param unit keyPrefix对象存入redis的有效时间单位
     * @param <R> 对象的泛型类型
     * @param <ID> 对象的id的泛型类型
     * @return
     */
    public <R, ID> R queryWithMutex(
            String keyPrefix,
            String lockKeyPrefix,
            ID id,
            Class<R> type,
            Function<ID, R> dbFallback,
            Long time,
            TimeUnit unit) {
        // 1.从redis查询商铺缓存
        String key = keyPrefix + id;
        String shopJson = stringRedisTemplate.opsForValue().get(key);
        // 2.判断是否存在
        if (StrUtil.isNotBlank(shopJson)) {
            // 3.存在，直接返回
            return JSONUtil.toBean(shopJson, type);
        }
        // 是空值，但是不为null，直接返回，防止缓存穿透
        if (shopJson != null) {
            return null;
        }
        // 4.缓存重构
        // 4.1 获取互斥锁
        String lockKey = lockKeyPrefix + id;
        R r = null;
        try {
            boolean isLock = tryLock(lockKey);
            // 4.2 判断是否获取成功
            if (!isLock) {
                // 4.3 失败，则休眠重试
                Thread.sleep(50);
                return queryWithMutex(keyPrefix, lockKeyPrefix, id, type, dbFallback, time, unit);
            }
            // 这里需要做双重检查，如果在获取锁期间已经有其他线程写入缓存，直接返回缓存数据
            shopJson = stringRedisTemplate.opsForValue().get(key);
            if (StrUtil.isNotBlank(shopJson)) {
                return JSONUtil.toBean(shopJson, type);
            }
            // 4.4 成功获取锁，根据id查询数据库
            r = dbFallback.apply(id);
            // 5.数据库中不存在，返回错误
            if (r == null) {
                // 防止缓存穿透，将空值写入redis，并设置有效期为2分钟
                stringRedisTemplate.opsForValue().set(key, "", RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            // 6.存在，写入redis
            this.set(key, r, time, unit);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 7.释放互斥锁
            unlock(lockKey);
        }
        return r;
    }

    /**
     * 逻辑过期方案，解决缓存击穿问题
     * 注意：需要提前进行缓存预热，否则会一直返回空
     * @param keyPrefix 对象存入redis的前缀，完整的key为 keyPrefix + id
     * @param lockKeyPrefix，对象互斥锁存入redis的前缀，完整的key为 lockKeyPrefix + id
     * @param id 需要获取的对象的id
     * @param type 对象的类型
     * @param dbFallback 如果缓存中没有该对象，需要从数据库中查询
     * @param time keyPrefix对象存入redis的有效时间
     * @param unit keyPrefix对象存入redis的有效时间单位
     * @param <R> 对象的泛型类型
     * @param <ID> 对象的id的泛型类型
     * @return
     */
    public <R, ID> R queryWithLogicalExpire(
            String keyPrefix,
            String lockKeyPrefix,
            ID id,
            Class<R> type,
            Function<ID, R> dbFallback,
            Long time,
            TimeUnit unit
    ) {
        // 1.从redis查询商铺缓存
        String key = keyPrefix + id;
        String redisDataJson = stringRedisTemplate.opsForValue().get(key);
        // 2.判断是否存在
        if (StrUtil.isBlank(redisDataJson)) {
            // 3.不存在，直接返回，所以我们必须要进行缓存预热，否则缓存中就会一直没有数据
            return null;
        }
        // 4.命中，需要先把json反序列化为对象
        RedisData redisData = JSONUtil.toBean(redisDataJson, RedisData.class);
        R r = JSONUtil.toBean((JSONObject) redisData.getData(), type);
        LocalDateTime expireTime = redisData.getExpireTime();
        // 5.判断是否过期
        if (expireTime.isAfter(LocalDateTime.now())) {
            // 5.1 未过期，直接返回店铺信息
            return r;
        }
        // 5.2 已过期，需要缓存重建
        // 6.缓存重建
        // 6.1 获取互斥锁
        String lockKey = lockKeyPrefix + id;
        boolean isLock = tryLock(lockKey);
        // 6.2 判断是否获取锁成功
        if (isLock) {
            // 6.3 开启子线程，重建缓存
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try {
                    // 查询数据库
                    R newR = dbFallback.apply(id);
                    // 重建缓存
                    this.setWithLogicalExpire(key, newR, time, unit);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    // 释放锁
                    unlock(lockKey);
                }
            });
        }
        // 6.4 没有获取到互斥锁的线程，就先返回过期的商铺信息
        // 互斥锁保证，缓存重建不会重复
        return r;
    }


    /**
     * 获取互斥锁
     * 利用SETNX命令的特性：key不存在添加，存在则不添加
     * @param key
     * @return
     */
    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", RedisConstants.LOCK_SHOP_TTL, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    /**
     * 释放互斥锁
     *
     * @param key
     * @return
     */
    private void unlock(String key) {
        stringRedisTemplate.delete(key);
    }
}
