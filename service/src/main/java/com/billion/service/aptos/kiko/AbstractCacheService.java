package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.billion.model.enums.CacheTsType;
import com.billion.model.service.ICacheService;
import com.billion.service.aptos.ContextService;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.Duration;

/**
 * @author liqiang
 */
public abstract class AbstractCacheService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements ICacheService<T> {

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

}