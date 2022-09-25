package com.billion.service.aptos.kiko;

import com.billion.model.entity.Handle;
import com.billion.model.service.ICacheService;

/**
 * @author liqiang
 */
public interface HandleService extends ICacheService<Handle> {

    /**
     * update
     *
     * @param account account
     * @return boolean
     */
    boolean update(String account);

    /**
     * getByAccount
     *
     * @param account account
     * @return Handle
     */
    Handle getByAccount(String account);

}