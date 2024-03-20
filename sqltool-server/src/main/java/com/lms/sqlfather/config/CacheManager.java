package com.lms.sqlfather.config;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.lms.redis.RedisCache;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author lms2000
 */
@Component
public class CacheManager {
    @Resource
    private RedisCache redisCache;
    /**
     * 本地缓存
     * 最大缓存存活时间为100分钟
     * 最大缓存数目为10000
     */
    Cache<String, Object> localCache = Caffeine.newBuilder()
            .expireAfterWrite(100, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        localCache.put(key, value);
        redisCache.setCacheObject(key,value,100,TimeUnit.MINUTES);
    }
    /**
     * 读缓存
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        // 先从本地缓存中获取
        Object value = localCache.getIfPresent(key);
        if (value != null) {
            return value;
        }

        // 本地缓存未命中，尝试从 Redis 获取
        value = redisCache.getCacheObject(key);
        if (value != null) {
            // 将 redis 的值写入到本地缓存
            localCache.put(key, value);
        }

        return value;
    }

    /**
     * 一处缓存
     *
     * @param key
     */
    public void delete(String key) {
        localCache.invalidate(key);
        redisCache.deleteObject(key);
    }
}
