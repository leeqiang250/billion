package com.billion.service.aptos.kiko;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftAttributeTypeMapper;
import com.billion.model.dto.NftAttribute;
import com.billion.model.dto.NftAttributeTypeMeta;
import com.billion.model.entity.NftAttributeMeta;
import com.billion.model.entity.NftAttributeType;
import com.billion.model.enums.Language;
import com.billion.service.aptos.AbstractCacheService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.billion.model.constant.RequestPath.EMPTY;

/**
 * @author jason
 */
@Service
public class NftAttributeTypeServiceImpl extends AbstractCacheService<NftAttributeTypeMapper, NftAttributeType> implements NftAttributeTypeService {

    @Resource
    LanguageService languageService;

    @Resource
    NftAttributeMetaService nftAttributeMetaService;

    @Override
    public NftAttribute getNftAttributeInfoByMetaId(Long metaId) {
        QueryWrapper<NftAttributeMeta> nftAttributeMetaQueryWrapper = new QueryWrapper<>();
        nftAttributeMetaQueryWrapper.lambda().eq(NftAttributeMeta::getId, metaId);
        var attributeMeta = nftAttributeMetaService.getOneThrowEx(nftAttributeMetaQueryWrapper);

        QueryWrapper<NftAttributeType> nftAttributeTypeQueryWrapper = new QueryWrapper<>();
        nftAttributeTypeQueryWrapper.lambda().eq(NftAttributeType::getId, attributeMeta.getNftAttributeTypeId());
        var attributeType = super.getOneThrowEx(nftAttributeTypeQueryWrapper);

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
        var attributeTypes = super.list(nftAttributeTypeQueryWrapper);
        attributeTypes.forEach(nftAttributeType -> {
            var nftAttributeTypeMap = JSONObject.parseObject(JSONObject.toJSONString(nftAttributeType), Map.class);

            var nftAttributeMetaQueryWrapper = new QueryWrapper<NftAttributeMeta>();
            nftAttributeMetaQueryWrapper.lambda().eq(NftAttributeMeta::getNftAttributeTypeId, nftAttributeType.getId());
            nftAttributeMetaQueryWrapper.lambda().orderByAsc(NftAttributeMeta::getId);
            var nftAttributeMetas = nftAttributeMetaService.list(nftAttributeMetaQueryWrapper);
            nftAttributeMetas.forEach(nftAttributeMeta -> {
                var nftAttributeMetaMap = JSONObject.parseObject(JSONObject.toJSONString(nftAttributeMeta), Map.class);
                nftAttributeTypeMap.putAll(nftAttributeMetaMap);

                var nftAttributeTypeMeta = JSONObject.parseObject(JSONObject.toJSONString(nftAttributeTypeMap), NftAttributeTypeMeta.class);

                var languageEn = this.languageService.getByLanguageKey(Language.EN, nftAttributeTypeMeta.getClassName());
                nftAttributeTypeMeta.setClassNameEn(Objects.isNull(languageEn) ? EMPTY : languageEn.getValue());
                var languageZhTC = this.languageService.getByLanguageKey(Language.ZH_TC, nftAttributeTypeMeta.getClassName());
                nftAttributeTypeMeta.setClassNameZhTC(Objects.isNull(languageZhTC) ? EMPTY : languageZhTC.getValue());

                languageEn = this.languageService.getByLanguageKey(Language.EN, nftAttributeTypeMeta.getAttribute());
                nftAttributeTypeMeta.setAttributeEn(Objects.isNull(languageEn) ? EMPTY : languageEn.getValue());
                languageZhTC = this.languageService.getByLanguageKey(Language.ZH_TC, nftAttributeTypeMeta.getAttribute());
                nftAttributeTypeMeta.setAttributeZhTC(Objects.isNull(languageZhTC) ? EMPTY : languageZhTC.getValue());

                result.add(nftAttributeTypeMeta);
            });
        });

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateNftAttributeTypeMeta(List<NftAttributeTypeMeta> nftAttributeTypeMetas) {
        nftAttributeTypeMetas.forEach(nftAttributeTypeMeta -> {
            this.updateById(NftAttributeType.builder()
                    .id(Long.parseLong(nftAttributeTypeMeta.getNftAttributeTypeId()))
                    .nftGroupId(Long.parseLong(nftAttributeTypeMeta.getNftGroupId()))
                    .className(nftAttributeTypeMeta.getClassName())
                    .sort(Integer.parseInt(nftAttributeTypeMeta.getSort()))
                    .build());

            this.languageService.updateByLanguageKey(Language.EN, nftAttributeTypeMeta.getClassName(), nftAttributeTypeMeta.getClassNameEn());
            this.languageService.updateByLanguageKey(Language.ZH_TC, nftAttributeTypeMeta.getClassName(), nftAttributeTypeMeta.getAttributeZhTC());

            this.nftAttributeMetaService.updateById(NftAttributeMeta.builder()
                    .id(Long.parseLong(nftAttributeTypeMeta.getId()))
                    .nftAttributeTypeId(Long.parseLong(nftAttributeTypeMeta.getNftAttributeTypeId()))
                    .attribute(nftAttributeTypeMeta.getAttribute())
                    .value(nftAttributeTypeMeta.getValue())
                    .uri(nftAttributeTypeMeta.getUri())
                    .build());

            this.languageService.updateByLanguageKey(Language.EN, nftAttributeTypeMeta.getAttribute(), nftAttributeTypeMeta.getAttributeEn());
            this.languageService.updateByLanguageKey(Language.ZH_TC, nftAttributeTypeMeta.getAttribute(), nftAttributeTypeMeta.getAttributeZhTC());
        });
    }

    @Override
    public List<NftAttributeType> getNftAttributeTypeByNftGroupId(Long nftGroupId) {
        var result = new ArrayList<NftAttributeType>();
        var wrapper = new QueryWrapper<NftAttributeType>();
        wrapper.lambda().eq(NftAttributeType::getNftGroupId, nftGroupId);
        wrapper.lambda().orderByAsc(NftAttributeType::getSort);
        wrapper.lambda().orderByAsc(NftAttributeType::getId);
        return super.list(wrapper);
    }
}