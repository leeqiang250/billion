package com.billion.service.aptos.kiko;

import com.alibaba.fastjson2.JSONObject;
import com.billion.dao.aptos.kiko.NftInfoMapper;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftGroup;
import com.billion.model.entity.NftInfo;
import com.billion.service.aptos.AbstractCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class NftInfoServiceImpl extends AbstractCacheService<NftInfoMapper, NftInfo> implements NftInfoService {

    @Override
    public Map cacheMap(Context context) {
        return null;
    }

    @Override
    public NftInfo cacheById(Context context, Serializable id, Duration timeout) {
        String key = this.cacheByIdKey(null, id);

        Object value = this.getRedisTemplate().opsForValue().get(key);
        if (Objects.isNull(value)) {
            NftInfo e = this.getById(id);
            if (Objects.nonNull(e)) {
                value = e;

                this.getRedisTemplate().opsForValue().set(key, e, timeout);
            }
        } else {
            value = this.fromObject(value);
        }

        return (NftInfo) value;
    }

    /**
     * updateState
     * @param id
     * @param state
     * @return
     */
    @Override
    public NftInfo updateState(String id, Integer state) {
        NftInfo nftInfo = this.getById(id);
        if (!Objects.isNull(nftInfo)) {
            nftInfo.setState(state);

            this.updateById(nftInfo);

            this.deleteCache(this.cacheByIdKey(null, id));
        }
        return nftInfo;
    }
}