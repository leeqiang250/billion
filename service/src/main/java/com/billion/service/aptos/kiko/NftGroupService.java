package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.extension.service.IService;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftGroup;
import lombok.NonNull;

import java.util.Map;

/**
 * @author liqiang
 */
public interface NftGroupService extends IService<NftGroup>, RedisService<NftGroup> {

    /**
     * getAllById
     *
     * @param context context
     * @return Map
     */
    Map getAllById(@NonNull Context context);

    /**
     * getAllByMetaBody
     *
     * @param context context
     * @return Map
     */
    Map getAllByMetaBody(@NonNull Context context);

    /**
     * getById
     *
     * @param id      id
     * @param context context
     * @return Object
     */
    Object getById(@NonNull Long id, @NonNull Context context);

    /**
     * getByMetaBody
     *
     * @param meta    meta
     * @param body    body
     * @param context context
     * @return Object
     */
    Object getByMetaBody(@NonNull String meta, @NonNull String body, @NonNull Context context);

}
