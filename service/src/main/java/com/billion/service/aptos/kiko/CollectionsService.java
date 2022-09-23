package com.billion.service.aptos.kiko;

import com.billion.model.entity.Collections;
import com.billion.model.service.ICacheService;

/**
 * @author liqiang
 */
public interface CollectionsService extends ICacheService<Collections> {

    /**
     * update
     *
     * @param account account
     */
    void update(String account);

}