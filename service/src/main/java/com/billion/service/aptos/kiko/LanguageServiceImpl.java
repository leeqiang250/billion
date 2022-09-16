package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.LanguageMapper;
import com.billion.model.constant.RedisPathConstant;
import com.billion.model.dto.Context;
import com.billion.model.entity.Language;
import com.billion.service.aptos.ContextService;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liqiang
 */
@Service
public class LanguageServiceImpl extends RedisServiceImpl<LanguageMapper, Language> implements LanguageService {

    @Resource
    ContextService contextService;

    @Override
    @SuppressWarnings(value = {"rawtypes"})
    public Map getLanguage(@NonNull Context context) {
        Map map = this.getRedisTemplate().opsForHash().entries(RedisPathConstant.LANGUAGE + context.getLanguage());
        if (!map.isEmpty()) {
            return map;
        }
        QueryWrapper<Language> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Language::getLanguage, context.getLanguage());
        List<Language> list = this.getBaseMapper().selectList(wrapper);
        map = list.stream().collect(Collectors.toMap(Language::getKey, Language::getValue, (key1, key2) -> key2));
        if (ContextService.isProd()) {
            this.getRedisTemplate().opsForHash().putAll(RedisPathConstant.LANGUAGE + context.getLanguage(), map);
            this.getRedisTemplate().expire(RedisPathConstant.LANGUAGE + context.getLanguage(), Duration.ofHours(1L));
        }
        return map;
    }

}