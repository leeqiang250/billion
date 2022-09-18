package com.billion.service.aptos;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.billion.model.enums.CacheTsType;
import com.billion.model.model.IModel;
import com.billion.model.service.ICacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.Duration;

/**
 * @author liqiang
 */
@Slf4j
public abstract class AbstractCacheService<M extends BaseMapper<T>, T extends IModel> extends ServiceImpl<M, T> implements ICacheService<T> {

    @Resource
    RedisTemplate<Serializable, T> redisTemplate;

    @Override
    public RedisTemplate<Serializable, T> getRedisTemplate() {
        return this.redisTemplate;
    }

    @Override
    public Duration cacheSecond(CacheTsType cacheTsType) {
        return ContextService.getCacheTsTypeDurationMap().get(cacheTsType);
    }

    @Override
    public String cacheMapKey(String groupBy) {
        String key = ICacheService.super.cacheMapKey(groupBy);
        log.info("this.cacheMapKey class:[{}] redis key:[{}]", this.getClass().getSimpleName(), key);
        return key;
    }

    @Override
    public String cacheByIdKey(String groupBy, Serializable id) {
        String key = ICacheService.super.cacheByIdKey(groupBy, id);
        log.info("this.cacheByIdKey class:[{}] redis key:[{}]", this.getClass().getSimpleName(), key);
        return key;
    }

}