package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftGroupMapper;
import com.billion.model.constant.RedisPathConstant;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftGroup;
import com.billion.service.aptos.ContextService;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liqiang
 */
@Service
public class NftGroupServiceImpl extends RedisServiceImpl<NftGroupMapper, NftGroup> implements NftGroupService {

    @Override
    @SuppressWarnings(value = {"rawtypes"})
    public Map getAllById(@NonNull Context context) {
        String key = RedisPathConstant.NFT + context.getChain() + "::id";
        Map map = this.getRedisTemplate().opsForHash().entries(key);
        if (!map.isEmpty()) {
            return map;
        }

        QueryWrapper<NftGroup> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftGroup::getEnabled, Boolean.TRUE);
        List<NftGroup> list = this.getBaseMapper().selectList(wrapper);
        map = list.stream().collect(Collectors.toMap(NftGroup::getId, (e) -> e));
        this.getRedisTemplate().opsForHash().putAll(key, map);
        this.getRedisTemplate().expire(key, Duration.ofSeconds(ContextService.getCacheMiddle()));


        return map;
    }

    @Override
    @SuppressWarnings(value = {"rawtypes"})
    public Map getAllByMetaBody(@NonNull Context context) {
        String key = RedisPathConstant.NFT + context.getChain() + "::meta-body";
        Map map = this.getRedisTemplate().opsForHash().entries(key);
        if (!map.isEmpty()) {
            return map;
        }

        QueryWrapper<NftGroup> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftGroup::getEnabled, Boolean.TRUE);
        List<NftGroup> list = this.getBaseMapper().selectList(wrapper);
        map = list.stream().collect(Collectors.toMap(e -> e.getMeta() + "::" + e.getBody(), (e) -> e));
        this.getRedisTemplate().opsForHash().putAll(key, map);
        this.getRedisTemplate().expire(key, Duration.ofSeconds(ContextService.getCacheMiddle()));

        return map;
    }

    @Override
    public Object getById(@NonNull String id, @NonNull Context context) {
        return this.getAllById(context).get(id);
    }

    @Override
    public Object getById(@NonNull String meta, @NonNull String body, @NonNull Context context) {
        return this.getAllByMetaBody(context).get(meta + "::" + body);
    }

}