package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * @author liqiang
 */
public class RedisServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements RedisService<T> {

    @Resource
    RedisTemplate<Serializable, T> redisTemplate;

    @Override
    public RedisTemplate<Serializable, T> getRedisTemplate() {
        return this.redisTemplate;
    }

}