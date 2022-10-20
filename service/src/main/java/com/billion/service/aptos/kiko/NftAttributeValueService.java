package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.extension.service.IService;
import com.billion.model.dto.NftAttribute;
import com.billion.model.entity.NftAttributeValue;

import java.util.List;

/**
 * @author jason
 */
public interface NftAttributeValueService extends IService<NftAttributeValue> {

    /**
     * getNftAttributeForMint
     * @param nftMetaId
     * @return
     */
    public List<NftAttribute> getNftAttributeForMint(Long nftMetaId);


}
