package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.LanguageMapper;
import com.billion.model.dto.Context;
import com.billion.model.entity.Language;
import com.billion.model.enums.CacheTsType;
import com.billion.service.aptos.AbstractCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.billion.model.constant.RequestPath.DEFAULT_TEXT;

/**
 * @author liqiang
 */
@Slf4j
@Service
@SuppressWarnings({"rawtypes"})
public class LanguageServiceImpl extends AbstractCacheService<LanguageMapper, Language> implements LanguageService {

    @Override
    public Map cacheMap(Context context) {
        String key = this.cacheMapKey(context.getLanguage());

        Map map = this.getRedisTemplate().opsForHash().entries(key);
        if (!map.isEmpty()) {
            return map;
        }

        QueryWrapper<Language> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Language::getLanguage, context.getLanguage());
        List<Language> list = this.getBaseMapper().selectList(wrapper);

        map = list.stream().collect(Collectors.toMap(Language::getKey, Language::getValue, (key1, key2) -> key2));

        this.getRedisTemplate().opsForHash().putAll(key, map);
        this.getRedisTemplate().expire(key, this.cacheSecond(CacheTsType.MIDDLE));

        return map;
    }

    @Override
    public String getByKey(Context context, String id) {
        String key = this.cacheByIdKey(context.getLanguage(), id);

        Object value = this.getRedisTemplate().opsForHash().get(key, id);
        if (Objects.isNull(value)) {
            Boolean result = this.getRedisTemplate().hasKey(key);
            if (Objects.nonNull(result) || !result) {
                value = this.cacheMap(context).get(id);
                if (Objects.isNull(value)) {
                    value = DEFAULT_TEXT;
                    log.info("missing language:[{}] key:[{}]", context.getLanguage(), id);
                }
            }
        }

        return value.toString();
    }

    @Override
    public Map getByKeys(Context context, Set keys) {
        Map values = new HashMap(keys.size());
        Map map = this.cacheMap(context);
        keys.forEach(e -> {
            Object value = map.get(e);
            if (Objects.isNull(value)) {
                value = DEFAULT_TEXT;
                log.info("missing language:[{}] key:[{}]", context.getLanguage(), e);
            }

            values.put(e, value);
        });

        return values;
    }

}