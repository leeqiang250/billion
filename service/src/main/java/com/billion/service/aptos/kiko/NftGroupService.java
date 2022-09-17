package com.billion.service.aptos.kiko;

import com.billion.model.service.ICacheService;
import com.billion.model.service.IService;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftGroup;

import java.util.Map;

/**
 * @author liqiang
 */
public interface NftGroupService extends IService<NftGroup>, ICacheService<NftGroup> {

    /**
     * getAllById
     *
     * @param context context
     * @return Map
     */
    Map getAllById(Context context);

    /**
     * getAllByMetaBody
     *
     * @param context context
     * @return Map
     */
    Map getAllByMetaBody(Context context);

    /**
     * getById
     *
     * @param id      id
     * @param context context
     * @return Object
     */
    Object getById(Long id, Context context);

    /**
     * getByMetaBody
     *
     * @param meta    meta
     * @param body    body
     * @param context context
     * @return Object
     */
    Object getByMetaBody(String meta, String body, Context context);

}
