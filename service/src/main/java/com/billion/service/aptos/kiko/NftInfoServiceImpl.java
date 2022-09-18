package com.billion.service.aptos.kiko;

import com.billion.dao.aptos.kiko.NftInfoMapper;
import com.billion.model.constant.RedisPathConstant;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftInfo;
import com.billion.model.enums.CacheTsType;
import com.billion.service.aptos.AbstractCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class NftInfoServiceImpl extends AbstractCacheService<NftInfoMapper, NftInfo> implements NftInfoService {

//    @Override
//    public NftInfo cacheById(Context context, String redisKeyPrefix, Serializable id, Duration timeout) {
//        String key = RedisPathConstant.NFT + this.entityClass.toString() + "::" + context.getChain();
//
//        Object t = this.getRedisTemplate().opsForHash().get(key, id);
//        if (Objects.isNull(t)) {
//            NftInfo e = this.getById(id);
//            if (Objects.nonNull(e)) {
//                this.getRedisTemplate().opsForHash().put(key, e.getId(), e);
//                this.getRedisTemplate().expire(key, this.cacheSecond(CacheTsType.CACHE_TS_MIDDLE));
//
//                t = e;
//            }
//        } else {
//            log.info(t.getClass().toString());
//        }
//
//        return (NftInfo) t;
//    }

}