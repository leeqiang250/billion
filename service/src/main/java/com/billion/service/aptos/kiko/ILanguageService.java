package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.extension.service.IService;
import com.billion.model.entity.Language;

import java.util.Map;

/**
 * @author liqiang
 */
public interface ILanguageService extends IService<Language>, IRedisService<Language> {

    /**
     * getLanguage
     *
     * @param language language
     * @return Map
     */
    Map getLanguage(String language);

}