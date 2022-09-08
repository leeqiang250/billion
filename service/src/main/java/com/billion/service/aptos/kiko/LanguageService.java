package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.billion.model.constant.RedisPathConstant;
import com.billion.model.entity.Language;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liqiang
 */
public interface LanguageService extends IService<Language>, IRedisService<Language> {

    /**
     * getLanguage
     *
     * @param language language
     * @return Map
     */
    default Map getLanguage(String language) {
        Map languageMap = this.getRedisTemplate().opsForHash().entries(RedisPathConstant.LANGUAGE + language);
        if (!languageMap.isEmpty()) {
            return languageMap;
        }
        QueryWrapper<Language> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Language::getLanguage, language);
        List<Language> languages = this.getBaseMapper().selectList(wrapper);
        languageMap = languages.stream().collect(Collectors.toMap(Language::getKey, Language::getValue, (key1, key2) -> key2));
        this.getRedisTemplate().opsForHash().putAll(RedisPathConstant.LANGUAGE + language, languageMap);
        return languageMap;
    }

}