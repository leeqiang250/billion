package com.billion.service.aptos.kiko;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftGroupMapper;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftGroup;
import com.billion.model.enums.CacheTsType;
import com.billion.service.aptos.AbstractCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author liqiang
 */
@Slf4j
@Service
@SuppressWarnings({"all"})
public class NftGroupServiceImpl extends AbstractCacheService<NftGroupMapper, NftGroup> implements NftGroupService {

    @Resource
    LanguageService languageService;

    @Override
    public Map cacheMap(Context context) {
        String key = this.cacheMapKey("ids::" + context.getChain());

        Map map = this.getRedisTemplate().opsForHash().entries(key);
        if (!map.isEmpty()) {
            return map;
        }

        QueryWrapper<NftGroup> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftGroup::getEnabled, Boolean.TRUE);
        List<NftGroup> list = this.getBaseMapper().selectList(wrapper);

        changeLanguage(context, list);

        map = list.stream().collect(Collectors.toMap(e -> e.getId().toString(), (e) -> e));
        this.getRedisTemplate().opsForHash().putAll(key, map);
        this.getRedisTemplate().expire(key, this.cacheSecond(CacheTsType.CACHE_TS_MIDDLE));

        log.warn("cacheMap:{}", key);

        return map;
    }

    @Override
    public Map getAllByMetaBody(Context context) {
        String key = this.cacheMapKey("meta-body::" + context.getChain());
        Map map = this.getRedisTemplate().opsForHash().entries(key);
        if (!map.isEmpty()) {
            return map;
        }

        QueryWrapper<NftGroup> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftGroup::getEnabled, Boolean.TRUE);
        List<NftGroup> list = this.getBaseMapper().selectList(wrapper);

        changeLanguage(context, list);

        map = list.stream().collect(Collectors.toMap(e -> e.getMeta() + "-" + e.getBody(), (e) -> e));
        this.getRedisTemplate().opsForHash().putAll(key, map);
        this.getRedisTemplate().expire(key, this.cacheSecond(CacheTsType.CACHE_TS_MIDDLE));

        return map;
    }

    @Override
    public NftGroup cacheById(Context context, Serializable id) {
        String key = this.cacheByIdKey(context.getChain(), id);

        Object value = this.getRedisTemplate().opsForHash().get(key, id);
        if (Objects.isNull(value)) {
            Boolean result = this.getRedisTemplate().hasKey(key);
            if (Objects.nonNull(result) && !result) {
                value = this.cacheMap(context).get(id);
            }
        } else {
            value = JSONObject.parseObject(JSONObject.toJSONString(value), NftGroup.class);
        }

        return (NftGroup) value;
    }

    @Override
    public NftGroup getByMetaBody(Context context, String meta, String body) {
        String id = meta + "-" + body;
        String key = this.cacheByIdKey(context.getChain(), id);

        Object value = this.getRedisTemplate().opsForHash().get(key, id);
        if (Objects.isNull(value)) {
            Boolean result = this.getRedisTemplate().hasKey(key);
            if (Objects.nonNull(result) && !result) {
                value = this.getAllByMetaBody(context).get(id);
            }
        } else {
            value = JSONObject.parseObject(JSONObject.toJSONString(value), NftGroup.class);
        }

        return (NftGroup) value;
    }

    public void changeLanguage(Context context, List<NftGroup> list) {
        Set setDisplayName = list.stream().map(e -> e.getDisplayName()).collect(Collectors.toSet());
        Set setDescription = list.stream().map(e -> e.getDescription()).collect(Collectors.toSet());

        Map mapDisplayName = languageService.getByKeys(context, setDisplayName);
        Map mapDescription = languageService.getByKeys(context, setDescription);

        list.forEach(e -> {
            e.setDisplayName(mapDisplayName.get(e.getDisplayName()).toString());
            e.setDescription(mapDescription.get(e.getDescription()).toString());
        });
    }

}