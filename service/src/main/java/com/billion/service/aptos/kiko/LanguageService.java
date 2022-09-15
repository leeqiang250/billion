package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.extension.service.IService;
import com.billion.model.dto.Header;
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
     * @param header header
     * @return Map
     */
    Map getLanguage(@NonNull Header header);

}