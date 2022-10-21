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
    public List<NftAttribute> getNftAttributeForMint(Long nftMetaId);

    /**
     * getNftAttributeValueByMetaId
     * 经过国际化处理
     * @param context
     * @param nftMetaId
     * @return
     */
    public List<NftAttribute> getNftAttributeValueByMetaId(Context context, String nftMetaId);
}