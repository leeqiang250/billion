package com.billion.service.aptos.kiko;

import com.billion.model.dto.NftAttribute;
import com.billion.model.dto.NftAttributeTypeMeta;
import com.billion.model.entity.NftAttributeType;
import com.billion.model.service.ICacheService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author jason
 */
public interface NftAttributeTypeService extends ICacheService<NftAttributeType> {

    /**
     * getNftAttributeInfobyMetaId
     *
     * @param metaId metaId
     * @return NftAttribute
     */
    public NftAttribute getNftAttributeInfoByMetaId(Long metaId);

    /**
     * getNftAttributeTypeMetaByNftGroupId
     *
     * @param nftGroupId nftGroupId
     * @return List<NftAttributeTypeMeta>
     */
    List<NftAttributeTypeMeta> getNftAttributeTypeMetaByNftGroupId(Long nftGroupId);


    /**
     * updateNftAttributeTypeMeta
     *
     * @param nftAttributeTypeMetas nftAttributeTypeMetas
     */
    @Transactional(rollbackFor = Exception.class)
    void updateNftAttributeTypeMeta(List<NftAttributeTypeMeta> nftAttributeTypeMetas);

}