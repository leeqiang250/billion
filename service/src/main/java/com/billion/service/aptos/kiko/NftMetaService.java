package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Response;
import com.aptos.request.v1.model.TableTokenData;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftMeta;
import com.billion.model.service.ICacheService;

import java.io.Serializable;
import java.util.List;

/**
 * @author liqiang
 */
public interface NftMetaService extends ICacheService<NftMeta> {

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

    /**
     * getListByGroup
     * @param context
     * @param type
     * @param groupId
     * @return
     */
    List<NftMeta> getListByGroup(Context context, String type, String groupId);

}