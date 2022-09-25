package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.TableTokenData;
import com.billion.model.entity.NftInfo;
import com.billion.model.service.ICacheService;

import java.io.Serializable;

/**
 * @author liqiang
 */
public interface NftInfoService extends ICacheService<NftInfo> {

    /**
     * getTableTokenData
     *
     * @param id id
     * @return TableTokenData
     */
    TableTokenData getTableTokenData(Serializable id);

}