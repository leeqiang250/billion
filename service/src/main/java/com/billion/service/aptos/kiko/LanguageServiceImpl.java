package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.LanguageMapper;
import com.billion.model.constant.RedisPathConstant;
import com.billion.model.dto.Context;
import com.billion.model.entity.Language;
import com.billion.service.aptos.ContextService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static com.billion.model.constant.RequestPathConstant.DEFAULT_TEXT;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class LanguageServiceImpl extends AbstractRedisService<LanguageMapper, Language> implements LanguageService {

    @Resource
    ContextService contextService;

    @Override
    @SuppressWarnings({"rawtypes"})
    public Map getAll(@NonNull Context context) {
        String key = RedisPathConstant.LANGUAGE + context.getLanguage();
        Map map = this.getRedisTemplate().opsForHash().entries(key);
        if (!map.isEmpty()) {
            return map;
        }

        QueryWrapper<Language> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Language::getLanguage, context.getLanguage());
        List<Language> list = this.getBaseMapper().selectList(wrapper);
        map = list.stream().collect(Collectors.toMap(Language::getKey, Language::getValue, (key1, key2) -> key2));
        this.getRedisTemplate().opsForHash().putAll(key, map);
        this.getRedisTemplate().expire(key, Duration.ofSeconds(ContextService.getCacheMiddle()));

        return map;
    }

    @Override
    public String getByKey(@NonNull String key, @NonNull Context context) {
        Object value = this.getAll(context).get(key);
        if (Objects.isNull(value)) {
            value = DEFAULT_TEXT;
            log.info("missing language:[{}] key:[{}]", context.getLanguage(), key);
        }

        return value.toString();
    }

    @Override
    public Map getByKeys(@NonNull Set keys, @NonNull Context context) {
        Map values = new HashMap(keys.size());
        Map map = this.getAll(context);
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