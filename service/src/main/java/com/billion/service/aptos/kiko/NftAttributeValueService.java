package com.billion.service.aptos.kiko;

import com.billion.model.dto.NftAttribute;
import com.billion.model.entity.NftAttributeValue;
import com.billion.model.service.ICacheService;

import java.util.List;

/**
 * @author jason
 */
public interface NftAttributeValueService extends ICacheService<NftAttributeValue> {

    /**
     * getNftAttributeForMint
     *
     * @param nftMetaId
     * @return
     */
    public List<NftAttribute> getNftAttributeForMint(Long nftMetaId);

}