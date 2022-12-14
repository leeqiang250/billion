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
            attributeType.setValue(Hex.encode(attributeType.getValue()));
            resultList.add(attributeType);
        });

        return resultList;
    }

    @Override
    public List<NftAttribute> getNftAttributeValueByMetaId(Context context, String nftMetaId) {
        QueryWrapper<NftAttributeValue> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(NftAttributeValue::getNftMetaId, nftMetaId);
        var attributeValue = super.list(queryWrapper);

        List<NftAttribute> resultList = new ArrayList<>(attributeValue.size() + 3);
        if (context != null) {
            changeLanguage(context, resultList);
        }
        attributeValue.forEach(v -> {
            var attributeType = nftAttributeTypeService.getNftAttributeInfoByMetaId(v.getNftAttributeMetaId());
            resultList.add(attributeType);
        });

        return resultList;
    }

    @Override
    public List<NftAttributeValue> getByNftMetaId(Long nftMetaId) {
        var wrapper = new QueryWrapper<NftAttributeValue>();
        wrapper.lambda().eq(NftAttributeValue::getNftMetaId, nftMetaId);
        return super.list(wrapper);
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