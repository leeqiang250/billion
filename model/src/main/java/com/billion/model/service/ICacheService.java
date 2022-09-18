package com.billion.model.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.billion.model.dto.Context;
import com.billion.model.enums.CacheTsType;
import com.billion.model.model.IModel;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author liqiang
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public interface ICacheService<T extends IModel> extends IService<T> {

    /**
     * RedisTemplate
     *
     * @return RedisTemplate<Serializable, T>
     */
    RedisTemplate<Serializable, T> getRedisTemplate();

    /**
     * cacheSecond
     *
     * @param cacheTsType cacheTsType
     * @return long
     */
    Duration cacheSecond(CacheTsType cacheTsType);

    /**
     * list
     *
     * @param context context
     * @return List
     */
    default Collection<T> cacheList(Context context) {
        Map collection = this.cacheMap(context);

        return Objects.isNull(collection) ? null : collection.values();
    }

    /**
     * cacheMap
     *
     * @param context context
     * @return Map
     */
    default Map<Serializable, T> cacheMap(Context context) {
        String key = this.getClass().toString() + context.key() + "::id";
        Map map = this.getRedisTemplate().opsForHash().entries(key);
        if (!map.isEmpty()) {
            return map;
        }

        List<T> list = this.list();

        map = list.stream().collect(Collectors.toMap(e -> e.getId().toString(), (e) -> e));
        this.getRedisTemplate().opsForHash().putAll(key, map);
        this.getRedisTemplate().expire(key, this.cacheSecond(CacheTsType.CACHE_TS_MIDDLE));

        return map;
    }

    /**
     * getCacheById
     *
     * @param context        context
     * @param redisKeyPrefix redisKeyPrefix
     * @param id             id
     * @return T
     */
    default T cacheById(Context context, String redisKeyPrefix, Serializable id) {
        return this.cacheById(context, redisKeyPrefix, id, this.cacheSecond(CacheTsType.CACHE_TS_SHORT));
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
    default T cacheById(Context context, String redisKeyPrefix, Serializable id, Duration timeout) {
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