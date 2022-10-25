package com.billion.service.aptos.kiko;

import com.billion.model.service.ICacheService;
import com.billion.model.dto.Context;
import com.billion.model.entity.Language;

import java.util.Map;
import java.util.Set;

/**
 * @author liqiang
 */
public interface LanguageService extends ICacheService<Language> {

    /**
     * getByKey
     *
     * @param context context
     * @param key     key
     * @return String
     */
    String getByKey(Context context, String key);

    /**
     * getByKeys
     *
     * @param context context
     * @param keys    keys
     * @return Map
     */
    Map getByKeys(Context context, Set keys);

    /**
     * getByLanguageKey
     *
     * @param language language
     * @param key      key
     * @return Language
     */
    Language getByLanguageKey(com.billion.model.enums.Language language, String key);

}