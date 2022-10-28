package com.billion.service.aptos.kiko;

import com.billion.model.entity.NftAttributeMeta;
import com.billion.model.service.ICacheService;

import java.util.Collection;
import java.util.List;

/**
 * @author jason
 */
public interface NftAttributeMetaService extends ICacheService<NftAttributeMeta> {

    /**
     * getNftAttributeMetaByNftGroupId
     *
     * @param nftAttributeTypeIds nftAttributeTypeIds
     * @return List<NftAttributeMeta>
     */
    List<NftAttributeMeta> getNftAttributeMetaByNftGroupId(Collection<Long> nftAttributeTypeIds);

}