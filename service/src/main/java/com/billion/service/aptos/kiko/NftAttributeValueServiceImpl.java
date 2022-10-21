package com.billion.service.aptos.kiko;

import com.aptos.utils.Hex;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftAttributeValueMapper;
import com.billion.model.dto.Context;
import com.billion.model.dto.NftAttribute;
import com.billion.model.entity.NftAttributeValue;
import com.billion.service.aptos.AbstractCacheService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jason
 */
@Service
public class NftAttributeValueServiceImpl extends AbstractCacheService<NftAttributeValueMapper, NftAttributeValue> implements NftAttributeValueService {

    final NftAttribute TOKEN_BURNABLE_BY_CREATOR = NftAttribute.builder()
            .type("bool")
            .key("TOKEN_BURNABLE_BY_CREATOR")
            .value(Hex.encodeBoolean(Boolean.TRUE))
            .build();

    final NftAttribute TOKEN_BURNABLE_BY_OWNER = NftAttribute.builder()
            .type("bool")
            .key("TOKEN_BURNABLE_BY_OWNER")
            .value(Hex.encodeBoolean(Boolean.TRUE))
            .build();

    final NftAttribute TOKEN_PROPERTY_MUTATBLE = NftAttribute.builder()
            .type("bool")
            .key("TOKEN_PROPERTY_MUTATBLE")
            .value(Hex.encodeBoolean(Boolean.TRUE))
            .build();

    @Resource
    NftAttributeTypeService nftAttributeTypeService;

    @Resource
    LanguageService languageService;

    @Override
    public List<NftAttribute> getNftAttributeForMint(Long nftMetaId) {
        QueryWrapper<NftAttributeValue> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(NftAttributeValue::getNftMetaId, nftMetaId);
        var attributeValue = super.list(queryWrapper);

        List<NftAttribute> resultList = new ArrayList<>(attributeValue.size() + 3);
        attributeValue.forEach(v -> {
            var attributeType = nftAttributeTypeService.getNftAttributeInfoByMetaId(v.getNftAttributeMetaId());
            attributeType.setKey(Hex.encode(attributeType.getKey()));
            attributeType.setType(Hex.encode(attributeType.getType()));
            resultList.add(attributeType);
        });

        resultList.add(TOKEN_BURNABLE_BY_CREATOR);
        resultList.add(TOKEN_BURNABLE_BY_OWNER);
        resultList.add(TOKEN_PROPERTY_MUTATBLE);

        return resultList;
    }

    @Override
    public List<NftAttribute> getNftAttributeValueByMetaId(Context context, String nftMetaId) {
        QueryWrapper<NftAttributeValue> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(NftAttributeValue::getNftMetaId, nftMetaId);
        var attributeValue = super.list(queryWrapper);

        List<NftAttribute> resultList = new ArrayList<>(attributeValue.size() + 3);
        attributeValue.forEach(v -> {
            var attributeType = nftAttributeTypeService.getNftAttributeInfoByMetaId(v.getNftAttributeMetaId());
            resultList.add(attributeType);
        });

        changeLanguage(context, resultList);

        resultList.add(TOKEN_BURNABLE_BY_CREATOR);
        resultList.add(TOKEN_BURNABLE_BY_OWNER);
        resultList.add(TOKEN_PROPERTY_MUTATBLE);

        return resultList;
    }


    private void changeLanguage(Context context, List<NftAttribute> list) {
        Set setKey = list.stream().map(e -> e.getKey()).collect(Collectors.toSet());
        Set setType = list.stream().map(e -> e.getType()).collect(Collectors.toSet());

        Map mapKey = languageService.getByKeys(context, setKey);
        Map mapType = languageService.getByKeys(context, setType);

        list.forEach(e -> {
            e.setKey(mapKey.get(e.getKey()).toString());
            e.setType(mapType.get(e.getType()).toString());
        });
    }

}