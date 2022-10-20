package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.billion.dao.aptos.kiko.NftAttributeTypeMapper;
import com.billion.model.dto.NftAttribute;
import com.billion.model.entity.NftAttributeMeta;
import com.billion.model.entity.NftAttributeType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author jason
 */
@Service
public class NftAttributeTypeServiceImpl extends ServiceImpl<NftAttributeTypeMapper, NftAttributeType> implements NftAttributeTypeService {

    @Resource
    NftAttributeMetaService nftAttributeMetaService;

    @Override
    public NftAttribute getNftAttributeInfoByMetaId(Long metaId) {
        QueryWrapper<NftAttributeMeta> nftAttributeMetaQueryWrapper = new QueryWrapper<>();
        nftAttributeMetaQueryWrapper.lambda().eq(NftAttributeMeta::getId, metaId);
        var attributeMeta = nftAttributeMetaService.getOne(nftAttributeMetaQueryWrapper);

        QueryWrapper<NftAttributeType> nftAttributeTypeQueryWrapper = new QueryWrapper<>();
        nftAttributeTypeQueryWrapper.lambda().eq(NftAttributeType::getId, attributeMeta.getNftAttributeTypeId());
        var attributeType = this.getOne(nftAttributeTypeQueryWrapper);

        NftAttribute nftAttribute = NftAttribute.builder()
                .type(attributeType.getClassName())
                .key(attributeMeta.getAttribute())
                .value(attributeMeta.getValue())
                .build();

        return nftAttribute;
    }
}
