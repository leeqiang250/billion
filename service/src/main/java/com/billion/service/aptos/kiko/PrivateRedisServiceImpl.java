package com.billion.service.aptos.kiko;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author liqiang
 */

@Slf4j
@Service
@SuppressWarnings({"rawtypes", "unchecked"})
public class PrivateRedisServiceImpl implements PrivateRedisService {

    @Resource
    RedisTemplate redisTemplate;

    @Override
    public Map get() {
        Map map = new HashMap<>(100);

        Cursor cursor = this.redisTemplate.scan(ScanOptions.scanOptions().count(1000L).build());
        while (cursor.hasNext()) {
            Object key = cursor.next();
            Object value = null;

            DataType dataType = this.redisTemplate.type(key);
            if (Objects.nonNull(dataType)) {
                switch (dataType) {
                    case STRING: {
                        value = this.redisTemplate.opsForValue().get(key);
                        break;
                    }
                    case LIST: {
                        Long size = this.redisTemplate.opsForList().size(key);
                        if (Objects.nonNull(size)) {
                            value = this.redisTemplate.opsForList().range(key, 0L, size);
                        }
                        break;
                    }
                    case SET: {
                        Long size = this.redisTemplate.opsForSet().size(key);
                        if (Objects.nonNull(size)) {
                            value = this.redisTemplate.opsForSet().randomMembers(key, size);
                        }
                        break;
                    }
                    case HASH: {
                        value = this.redisTemplate.opsForHash().entries(key);
                        break;
                    }
                    default: {
                        value = dataType.code();
                        log.info("{} {}", key, dataType.code());
                        break;
                    }
                }
            }

            if (Objects.nonNull(value)) {
                map.put(key, value);
            }
        }

        return map;
    }

    @Override
    public Boolean delete(String key) {
        return this.redisTemplate.delete(key);
    }

}