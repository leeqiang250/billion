package com.billion.model.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.billion.model.dto.Context;
import com.billion.model.enums.CacheTsType;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.time.Duration;
import java.util.List;
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
     * list
     *
     * @param context context
     * @return List
     */
    default List<T> list(Context context) {
        return this.list();
    }

    /**
     * getCacheById
     *
     * @param context        context
     * @param redisKeyPrefix redisKeyPrefix
     * @param id             id
     * @return T
     */
    default T getCacheById(Context context, String redisKeyPrefix, Serializable id) {
        return this.getCacheById(context, redisKeyPrefix, id, this.cacheSecond(CacheTsType.CACHE_TS_SHORT));
    }

    /**
     * getCacheById
     *
     * @param context        context
     * @param redisKeyPrefix redisKeyPrefix
     * @param id             id
     * @param timeout        timeout
     * @return T
     */
    default T getCacheById(Context context, String redisKeyPrefix, Serializable id, Duration timeout) {
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