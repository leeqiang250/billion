package com.billion.service.aptos.kiko;

import com.billion.model.entity.BoxGroupCopy;
import com.billion.model.service.ICacheService;

/**
 * @author liqiang
 */
public interface BoxGroupCopyService extends ICacheService<BoxGroupCopy> {

    /**
     * initialize
     *
     * @return boolean
     */
    boolean initialize();

}