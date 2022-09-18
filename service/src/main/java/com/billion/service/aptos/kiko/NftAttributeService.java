package com.billion.service.aptos.kiko;

import com.billion.model.dto.Context;
import com.billion.model.entity.NftAttribute;
import com.billion.model.service.ICacheService;

import java.util.Collection;

/**
 * @author liqiang
 */
public interface NftAttributeService extends ICacheService<NftAttribute> {

    /**
     * getByGroupId
     *
     * @param context context
     * @param groupId groupId
     * @return Collection
     */
    Collection getByGroupId(Context context, String groupId);

}
