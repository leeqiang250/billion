package com.billion.service.aptos.kiko;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftAttributeTypeMapper;
import com.billion.model.dto.NftAttribute;
import com.billion.model.dto.NftAttributeTypeMeta;
import com.billion.model.entity.NftAttributeMeta;
import com.billion.model.entity.NftAttributeType;
import com.billion.service.aptos.AbstractCacheService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jason
 */
@Service
public class NftAttributeTypeServiceImpl extends AbstractCacheService<NftAttributeTypeMapper, NftAttributeType> implements NftAttributeTypeService {

    @Resource
    NftAttributeMetaService nftAttributeMetaService;

    @Override
    public NftAttribute getNftAttributeInfoByMetaId(Long metaId) {
        QueryWrapper<NftAttributeMeta> nftAttributeMetaQueryWrapper = new QueryWrapper<>();
        nftAttributeMetaQueryWrapper.lambda().eq(NftAttributeMeta::getId, metaId);
        var attributeMeta = nftAttributeMetaService.getOneThrowEx(nftAttributeMetaQueryWrapper);

        QueryWrapper<NftAttributeType> nftAttributeTypeQueryWrapper = new QueryWrapper<>();
        nftAttributeTypeQueryWrapper.lambda().eq(NftAttributeType::getId, attributeMeta.getNftAttributeTypeId());
        var attributeType = this.getOneThrowEx(nftAttributeTypeQueryWrapper);

        return NftAttribute.builder()
                .type(attributeType.getClassName())
                .key(attributeMeta.getAttribute())
                .value(attributeMeta.getValue())
                .build();
    }

    @Override
    public List<NftAttributeTypeMeta> getNftAttributeTypeMetaByNftGroupId(Long nftGroupId) {
        var result = new ArrayList<NftAttributeTypeMeta>();
        var nftAttributeTypeQueryWrapper = new QueryWrapper<NftAttributeType>();
        nftAttributeTypeQueryWrapper.lambda().eq(NftAttributeType::getNftGroupId, nftGroupId);
        nftAttributeTypeQueryWrapper.lambda().orderByAsc(NftAttributeType::getSort);
        nftAttributeTypeQueryWrapper.lambda().orderByAsc(NftAttributeType::getId);
        var attributeTypes = this.list(nftAttributeTypeQueryWrapper);
        attributeTypes.forEach(nftAttributeType -> {
            var nftAttributeTypeMap = JSONObject.parseObject(JSONObject.toJSONString(nftAttributeType), Map.class);

            var nftAttributeMetaQueryWrapper = new QueryWrapper<NftAttributeMeta>();
            nftAttributeMetaQueryWrapper.lambda().eq(NftAttributeMeta::getNftAttributeTypeId, nftAttributeType.getId());
            nftAttributeMetaQueryWrapper.lambda().orderByAsc(NftAttributeMeta::getId);
            var nftAttributeMetas = nftAttributeMetaService.list(nftAttributeMetaQueryWrapper);
            nftAttributeMetas.forEach(nftAttributeMeta -> {
                var nftAttributeMetaMap = JSONObject.parseObject(JSONObject.toJSONString(nftAttributeMeta), Map.class);
                nftAttributeTypeMap.putAll(nftAttributeMetaMap);
                result.add(JSONObject.parseObject(JSONObject.toJSONString(nftAttributeTypeMap), NftAttributeTypeMeta.class));
            });
        });

        return result;
    }

}