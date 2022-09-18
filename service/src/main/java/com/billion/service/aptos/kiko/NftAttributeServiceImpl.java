package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftAttributeMapper;
import com.billion.model.constant.RedisPathConstant;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftAttribute;
import com.billion.model.enums.CacheTsType;
import com.billion.service.aptos.AbstractCacheService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liqiang
 */
@Service
public class NftAttributeServiceImpl extends AbstractCacheService<NftAttributeMapper, NftAttribute> implements NftAttributeService {

    @Override
    public Collection getByGroupId(Context context, String key) {
        String path = RedisPathConstant.NFT + key + "::attribute";
        Map map = this.getRedisTemplate().opsForHash().entries(path);
        if (!map.isEmpty()) {
            return map.values();
        }

        QueryWrapper<NftAttribute> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftAttribute::getGroupId, key);
        List<NftAttribute> list = this.getBaseMapper().selectList(wrapper);

        map = list.stream().collect(Collectors.toMap(e -> e.getId().toString(), (e) -> e));

        this.getRedisTemplate().opsForHash().putAll(path, map);
        this.getRedisTemplate().expire(path, this.cacheSecond(CacheTsType.CACHE_TS_MIDDLE));

        return map.values();
    }

}