package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.extension.service.IService;
import com.billion.model.dto.Context;
import com.billion.model.entity.Language;
import lombok.NonNull;

import java.util.Map;

/**
 * @author liqiang
 */
public interface LanguageService extends IService<Language>, RedisService<Language> {

    /**
     * getLanguage
     *
     * @param context context
     * @return Map
     */
    Map getLanguage(@NonNull Context context);

}