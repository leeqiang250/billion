package com.billion.service.aptos.kiko;

import com.billion.model.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class DistributedLockService {

    static final Long RELEASE_SUCCESS = 1L;

    @Resource
    RedisTemplate redisTemplate;

    public void tryGetDistributedLock(String lockKey, String requestId, long expireTime, Consumer consumer) {
        try {
            if (this.tryGetDistributedLock(lockKey, requestId, expireTime)) {
                log.info("Get Distributed Lock {} {}", lockKey, requestId);
                consumer.accept(null);
            }
        } catch (Exception e) {
            log.error("{}", e);
            throw new BizException(e.getMessage());
        } finally {
            this.releaseDistributedLock(lockKey, requestId);
        }
    }

    boolean tryGetDistributedLock(String lockKey, String requestId, long expireTime) {
        return Boolean.TRUE.equals(this.redisTemplate.opsForValue().setIfAbsent(lockKey, requestId, Duration.ofMillis(expireTime)));
    }

    boolean releaseDistributedLock(String lockKey, String requestId) {
        return RELEASE_SUCCESS.equals(this.redisTemplate.execute(
                new DefaultRedisScript<>("if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end", Long.class),
                List.of(lockKey),
                requestId
        ));
    }

}