package com.billion.service.aptos.kiko;

import com.billion.model.dto.Context;
import com.billion.model.entity.NftAttribute;
import com.billion.model.service.IService;

import java.util.Collection;

/**
 * @author liqiang
 */
public interface NftAttributeService extends IService<NftAttribute> {

    /**
     * getByGroupId
     *
     * @param context context
     * @param groupId groupId
     * @return Collection
     */
    Collection getByGroupId(Context context, String groupId);

}
