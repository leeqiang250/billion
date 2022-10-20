package com.billion.service.aptos.kiko;

import com.billion.model.dto.NftAttribute;
import com.billion.model.entity.NftAttributeType;
import com.billion.model.service.ICacheService;

/**
 * @author jason
 */
public interface NftAttributeTypeService extends ICacheService<NftAttributeType> {

    /**
     * getNftAttributeInfobyMetaId
     *
     * @param metaId
     * @return
     */
    public NftAttribute getNftAttributeInfoByMetaId(Long metaId);

}