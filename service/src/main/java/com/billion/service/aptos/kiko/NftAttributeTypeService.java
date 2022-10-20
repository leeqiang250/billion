package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.extension.service.IService;
import com.billion.model.dto.NftAttribute;
import com.billion.model.entity.NftAttributeType;

/**
 * @author jason
 */
public interface NftAttributeTypeService extends IService<NftAttributeType> {

    /**
     * getNftAttributeInfobyMetaId
     *
     * @param metaId
     * @return
     */
    public NftAttribute getNftAttributeInfoByMetaId(Long metaId);

}