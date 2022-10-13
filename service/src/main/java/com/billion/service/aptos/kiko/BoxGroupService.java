package com.billion.service.aptos.kiko;

import com.billion.model.entity.BoxGroup;
import com.billion.model.service.ICacheService;

/**
 * @author liqiang
 */
public interface BoxGroupService extends ICacheService<BoxGroup> {

    /**
     * initialize
     *
     * @return boolean
     */
    boolean initialize();

    /**
     * initializeMarket
     *
     * @return boolean
     */
    boolean initializeMarket();

}