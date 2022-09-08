package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;

/**
 * @author liqiang
 */
public interface IRedisService<T> extends IService<T> {

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
     * @param id id
     * @return T
     */
    default T getCacheById(String redisKeyPrefix, Serializable id) {
        return this.getCacheById(redisKeyPrefix, id, Duration.ofHours(1L));
    }

    /**
     * getCacheById
     *
     * @param redisKeyPrefix redisKeyPrefix
     * @param id id
     * @param timeout timeout
     * @return T
     */
    default T getCacheById(String redisKeyPrefix, Serializable id, Duration timeout) {
        T t = this.getRedisTemplate().opsForValue().get(redisKeyPrefix + id);
        if (Objects.isNull(t)) {
            t = this.getById(id);
        }
        this.getRedisTemplate().opsForValue().set(redisKeyPrefix + id, t, timeout);
        return t;
    }

}