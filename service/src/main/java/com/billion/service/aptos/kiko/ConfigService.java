package com.billion.service.aptos.kiko;

import com.billion.model.dto.Context;
import com.billion.model.service.ICacheService;

/**
 * @author liqiang
 */
public interface ConfigService extends ICacheService<com.billion.model.entity.Config> {

    /**
     * get
     *
     * @param context context
     * @return Config
     */
    com.billion.model.dto.Config get(Context context);

}