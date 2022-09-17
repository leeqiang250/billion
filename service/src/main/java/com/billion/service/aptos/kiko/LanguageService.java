package com.billion.service.aptos.kiko;

import com.billion.model.service.ICacheService;
import com.billion.model.service.IService;
import com.billion.model.dto.Context;
import com.billion.model.entity.Language;

import java.util.Map;
import java.util.Set;

/**
 * @author liqiang
 */
public interface LanguageService extends IService<Language>, ICacheService<Language> {

    /**
     * getLanguage
     *
     * @param context context
     * @return Map
     */
    Map getAll(Context context);

    /**
     * getLanguageByKey
     *
     * @param key     key
     * @param context context
     * @return String
     */
    String getByKey(String key, Context context);

    /**
     * getByKeys
     *
     * @param keys    keys
     * @param context context
     * @return Map
     */
    Map getByKeys(Set keys, Context context);

}