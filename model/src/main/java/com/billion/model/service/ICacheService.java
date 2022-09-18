package com.billion.model.service;

import com.alibaba.fastjson2.JSONObject;
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
@SuppressWarnings({"all"})
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

    default String cacheMapKey(Context context) {
        return this.getEntityClass().toString() + context.key() + "::ids";
    }

    default String cacheByIdKey(Context context, Serializable id) {
        return this.getEntityClass().toString() + context.key() + "::id::" + id.toString();
    }

    /**
     * list
     *
     * @param context context
     * @return List
     */
    default Collection cacheList(Context context) {
        Map collection = this.cacheMap(context);

        return Objects.isNull(collection) ? null : collection.values();
    }

    /**
     * cacheMap
     *
     * @param context context
     * @return Map
     */
    default Map cacheMap(Context context) {
        String key = this.cacheMapKey(context);

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
     * @param context context
     * @param id      id
     * @return T
     */
    default T cacheById(Context context, Serializable id) {
        return this.cacheById(context, id, this.cacheSecond(CacheTsType.CACHE_TS_SHORT));
    }

    /**
     * getCacheById
     *
     * @param context context
     * @param id      id
     * @param timeout timeout
     * @return T
     */
    default T cacheById(Context context, Serializable id, Duration timeout) {
        String key = this.cacheByIdKey(context, id);

        Object t = this.getRedisTemplate().opsForValue().get(key);
        if (Objects.isNull(t)) {
            T e = this.getById(id);
            if (Objects.nonNull(e)) {
                this.getRedisTemplate().opsForValue().set(key, e, timeout);
                t = e;
            }
        } else {
            t = JSONObject.parseObject(JSONObject.toJSONString(t), this.getEntityClass());
        }

        return (T) t;
    }

}