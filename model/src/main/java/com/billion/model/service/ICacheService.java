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

    default String cacheMapKey(String groupBy) {
        return Objects.isNull(groupBy)
                ? this.getEntityClass().toString() + "::ids"
                : this.getEntityClass().toString() + "::" + groupBy + "::ids";
    }

    default String cacheByIdKey(String groupBy, Serializable id) {
        return Objects.isNull(groupBy)
                ? this.getEntityClass().toString() + "::id::" + id.toString()
                : this.getEntityClass().toString() + "::" + groupBy + "::id::" + id.toString();
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
        String key = this.cacheMapKey(null);

        Map map = this.getRedisTemplate().opsForHash().entries(key);
        if (!map.isEmpty()) {
            return map;
        }

        List<T> list = this.list();

        map = list.stream().collect(Collectors.toMap(e -> e.getId().toString(), (e) -> e));
        this.getRedisTemplate().opsForHash().putAll(key, map);
        this.getRedisTemplate().expire(key, this.cacheSecond(CacheTsType.MIDDLE));

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
        return this.cacheById(context, id, this.cacheSecond(CacheTsType.SHORT));
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
        String key = this.cacheByIdKey(null, id);

        Object value = this.getRedisTemplate().opsForValue().get(key);
        if (Objects.isNull(value)) {
            T t = this.getById(id);
            if (Objects.nonNull(t)) {
                value = t;

                this.getRedisTemplate().opsForValue().set(key, t, timeout);
            }
        } else {
            this.fromObject(value);
        }

        return (T) value;
    }

    /**
     * fromObject
     *
     * @param value value
     * @return T
     */
    default T fromObject(Object value) {
        //TODO 将来修复
        return JSONObject.parseObject(JSONObject.toJSONString(value), this.getEntityClass());
    }

}