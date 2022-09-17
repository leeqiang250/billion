package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftGroupMapper;
import com.billion.model.constant.RedisPathConstant;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftGroup;
import com.billion.service.aptos.ContextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class NftGroupServiceImpl extends AbstractRedisService<NftGroupMapper, NftGroup> implements NftGroupService {

    @Resource
    LanguageService languageService;

    @Override
    @SuppressWarnings({"rawtypes"})
    public Map getAllById(Context context) {
        String key = RedisPathConstant.NFT + context.getChain() + "::id";
        Map map = this.getRedisTemplate().opsForHash().entries(key);
        if (!map.isEmpty()) {
            return map;
        }

        QueryWrapper<NftGroup> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftGroup::getEnabled, Boolean.TRUE);
        List<NftGroup> list = this.getBaseMapper().selectList(wrapper);

        changeLanguage(list, context);

        map = list.stream().collect(Collectors.toMap(e -> e.getId(), (e) -> e));
        this.getRedisTemplate().opsForHash().putAll(key, map);
        this.getRedisTemplate().expire(key, Duration.ofSeconds(ContextService.getCacheMiddle()));

        return map;
    }

    @Override
    @SuppressWarnings({"rawtypes"})
    public Map getAllByMetaBody(Context context) {
        String key = RedisPathConstant.NFT + context.getChain() + "::meta-body";
        Map map = this.getRedisTemplate().opsForHash().entries(key);
        if (!map.isEmpty()) {
            return map;
        }

        QueryWrapper<NftGroup> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftGroup::getEnabled, Boolean.TRUE);
        List<NftGroup> list = this.getBaseMapper().selectList(wrapper);

        changeLanguage(list, context);

        map = list.stream().collect(Collectors.toMap(e -> e.getMeta() + "::" + e.getBody(), (e) -> e));
        this.getRedisTemplate().opsForHash().putAll(key, map);
        this.getRedisTemplate().expire(key, Duration.ofSeconds(ContextService.getCacheMiddle()));

        return map;
    }

    @Override
    public Object getById(Long id, Context context) {
        return this.getAllById(context).get(id);
    }

    @Override
    public Object getByMetaBody(String meta, String body, Context context) {
        return this.getAllByMetaBody(context).get(meta + "::" + body);
    }


    public void changeLanguage(List<NftGroup> list, Context context) {
        Set setDisplayName = list.stream().map(e -> e.getDisplayName()).collect(Collectors.toSet());
        Set setDescription = list.stream().map(e -> e.getDescription()).collect(Collectors.toSet());

        Map mapDisplayName = languageService.getByKeys(setDisplayName, context);
        Map mapDescription = languageService.getByKeys(setDescription, context);

        list.forEach(e -> {
            e.setDisplayName(mapDisplayName.get(e.getDisplayName()).toString());
            e.setDescription(mapDescription.get(e.getDescription()).toString());
        });
    }

}