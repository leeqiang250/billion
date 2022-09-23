package com.billion.service.aptos.kiko;

import com.billion.model.service.ICacheService;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftGroup;

import java.util.Map;

/**
 * @author liqiang
 */
@SuppressWarnings({"rawtypes"})
public interface NftGroupService extends ICacheService<NftGroup> {

    /**
     * getAllByMetaBody
     *
     * @param context context
     * @return Map
     */
    Map getAllByMetaBody(Context context);

    /**
     * getByMetaBody
     *
     * @param context context
     * @param meta    meta
     * @param body    body
     * @return NftGroup
     */
    NftGroup getByMetaBody(Context context, String meta, String body);

    /**
     * updateSupply
     *
     * @param id id
     * @return NftGroup
     */
    NftGroup updateSupply(String id);

}