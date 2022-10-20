package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftAttributeMapper;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftAttribute;
import com.billion.model.entity.NftClass;
import com.billion.model.entity.NftGroup;
import com.billion.model.enums.CacheTsType;
import com.billion.service.aptos.AbstractCacheService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liqiang
 */
@Service
public class NftAttributeServiceImpl extends AbstractCacheService<NftAttributeMapper, NftAttribute> implements NftAttributeService {

    @Resource
    LanguageService languageService;

    @Override
    public Collection getByGroupId(Context context, String groupId) {
        String key = this.cacheByIdKey(null, groupId);

        Map map = this.getRedisTemplate().opsForHash().entries(key);
        if (!map.isEmpty()) {
            return map.values();
        }

        QueryWrapper<NftAttribute> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftAttribute::getNftClassId, groupId);
        List<NftAttribute> list = super.list(wrapper);

        map = list.stream().collect(Collectors.toMap(e -> e.getId().toString(), (e) -> e));

        this.getRedisTemplate().opsForHash().putAll(key, map);
        this.getRedisTemplate().expire(key, this.cacheSecond(CacheTsType.MIDDLE));

        return map.values();
    }

    @Override
    public List<NftAttribute> getByClassId(Context context, String classId) {
        QueryWrapper<NftAttribute> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftAttribute::getNftClassId, classId);
        List<NftAttribute> list = super.list(wrapper);
        if (!Objects.isNull(context)) {
            changeLanguage(context, list);
        }

        return list;
    }

    private void changeLanguage(Context context, List<NftAttribute> list) {
        Set setAttribute = list.stream().map(e -> e.getAttribute()).collect(Collectors.toSet());
        Set setValue = list.stream().map(e -> e.getValue()).collect(Collectors.toSet());

        Map mapAttribute = languageService.getByKeys(context, setAttribute);
        Map mapValue = languageService.getByKeys(context, setValue);

        list.forEach(e -> {
            e.setAttribute(mapAttribute.get(e.getAttribute()).toString());
            e.setValue(mapValue.get(e.getValue()).toString());
        });
    }

}