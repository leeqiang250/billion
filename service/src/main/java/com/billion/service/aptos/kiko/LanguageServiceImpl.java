package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.LanguageMapper;
import com.billion.model.constant.RedisPathConstant;
import com.billion.model.entity.Language;
import com.billion.service.aptos.ContextService;
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
public class LanguageServiceImpl extends RedisServiceImpl<LanguageMapper, Language> implements ILanguageService {

    @Resource
    ContextService contextService;

    @Override
    @SuppressWarnings(value = {"rawtypes"})
    public Map getLanguage(String language) {
        Map languageMap = this.getRedisTemplate().opsForHash().entries(RedisPathConstant.LANGUAGE + language);
        if (!languageMap.isEmpty()) {
            return languageMap;
        }
        QueryWrapper<Language> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Language::getLanguage, language);
        List<Language> languages = this.getBaseMapper().selectList(wrapper);
        languageMap = languages.stream().collect(Collectors.toMap(Language::getKey, Language::getValue, (key1, key2) -> key2));
        if (this.contextService.isProd()) {
            this.getRedisTemplate().opsForHash().putAll(RedisPathConstant.LANGUAGE + language, languageMap);
            this.getRedisTemplate().expire(RedisPathConstant.LANGUAGE + language, Duration.ofHours(1L));
        }
        return languageMap;
    }
}