package com.billion.model.service;

import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;

/**
 * @author liqiang
 */
public interface ICacheService<T> extends IService<T> {

    /**
     * cacheSecond
     *
     * @param cacheTsType cacheTsType
     * @return long
     */
    Duration cacheSecond(CacheTsType cacheTsType);

    /**
     * RedisTemplate
     *
     * @return RedisTemplate<Serializable, T>
     */
    RedisTemplate<Serializable, T> getRedisTemplate();

    /**
     * getCacheById
     *
     * @param redisKeyPrefix redisKeyPrefix
     * @param id             id
     * @return T
     */
    default T getCacheById(String redisKeyPrefix, Serializable id) {
        return this.getCacheById(redisKeyPrefix, id, this.cacheSecond(CacheTsType.CACHE_TS_SHORT));
    }

    /**
     * getCacheById
     *
     * @param redisKeyPrefix redisKeyPrefix
     * @param id             id
     * @param timeout        timeout
     * @return T
     */
    default T getCacheById(String redisKeyPrefix, Serializable id, Duration timeout) {
        T t = this.getRedisTemplate().opsForValue().get(redisKeyPrefix + id);
        if (Objects.isNull(t)) {
            t = this.getById(id);
        }
        if (Objects.nonNull(t)) {
            this.getRedisTemplate().opsForValue().set(redisKeyPrefix + id, t, timeout);
        }

        return t;
    }

}