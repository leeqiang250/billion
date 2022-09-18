package com.billion.service.aptos.kiko;

import com.billion.model.service.ICacheService;
import com.billion.model.dto.Context;
import com.billion.model.entity.Contract;

import java.util.Map;

/**
 * @author liqiang
 */
public interface ContractService extends ICacheService<Contract> {

    /**
     * getContract
     *
     * @param context context
     * @return Map
     */
    Map getAll(Context context);

}
