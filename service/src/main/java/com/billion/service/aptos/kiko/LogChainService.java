package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.RequestInfo;
import com.billion.model.entity.LogChain;
import com.billion.model.service.ICacheService;

/**
 * @author liqiang
 */
public interface LogChainService extends ICacheService<LogChain> {

    void add(RequestInfo info);

}
