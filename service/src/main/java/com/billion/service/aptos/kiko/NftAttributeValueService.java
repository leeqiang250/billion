package com.billion.service.aptos.kiko;

import com.billion.model.dto.Context;
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
    List<NftAttribute> getNftAttributeForMint(Long nftMetaId);

    /**
     * getNftAttributeValueByMetaId
     *
     * @param context
     * @param nftMetaId
     * @return
     */
    List<NftAttribute> getNftAttributeValueByMetaId(Context context, String nftMetaId);

    /**
     * getByNftMetaId
     *
     * @param nftMetaId
     * @return
     */
    List<NftAttributeValue> getByNftMetaId(Long nftMetaId);

}