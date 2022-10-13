package com.billion.service.aptos.kiko;

import com.billion.model.entity.BoxGroup;
import com.billion.model.service.ICacheService;

import java.io.Serializable;

/**
 * @author liqiang
 */
public interface BoxGroupService extends ICacheService<BoxGroup> {

    /**
     * initialize
     *
     * @param id id
     * @return boolean
     */
    boolean initialize(Serializable id);

    /**
     * initializeMarket
     *
     * @return boolean
     */
    boolean initializeMarket();

}