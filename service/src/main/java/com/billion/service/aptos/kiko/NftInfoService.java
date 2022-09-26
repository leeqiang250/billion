package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Response;
import com.aptos.request.v1.model.TableTokenData;
import com.billion.model.entity.NftInfo;
import com.billion.model.service.ICacheService;

import java.io.Serializable;

/**
 * @author liqiang
 */
public interface NftInfoService extends ICacheService<NftInfo> {

    /**
     * mint
     *
     * @param groupId groupId
     * @return boolean
     */
    boolean mint(Serializable groupId);

    /**
     * getTableTokenData
     *
     * @param id id
     * @return Response<TableTokenData>
     */
    Response<TableTokenData> getTableTokenData(Serializable id);

}