package com.billion.service.aptos.kiko;

import com.billion.model.service.ICacheService;
import com.billion.model.entity.Contract;

/**
 * @author liqiang
 */
public interface ContractService extends ICacheService<Contract> {

    /**
     * getByName
     *
     * @param name name
     * @return Contract
     */
    Contract getByName(String name);

}