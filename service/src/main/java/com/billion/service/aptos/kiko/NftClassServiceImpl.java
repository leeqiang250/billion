package com.billion.service.aptos.kiko;

import com.aptos.utils.Hex;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftClassMapper;
import com.billion.model.dto.Context;
import com.billion.model.dto.NftClassDto;
import com.billion.model.entity.NftAttribute;
import com.billion.model.entity.NftClass;
import com.billion.model.entity.NftGroup;
import com.billion.model.enums.NftPropertyType;
import com.billion.service.aptos.AbstractCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.billion.model.constant.RequestPath.EMPTY;


/**
 * @author jason
 */
@Service
public class NftClassServiceImpl extends AbstractCacheService<NftClassMapper, NftClass> implements NftClassService {
    @Resource
    NftAttributeService nftAttributeService;

    @Resource
    LanguageService languageService;


    @Override
    public List getClassByGroupId(Context context, String groupId) {
        QueryWrapper<NftClass> wrapper = new QueryWrapper<>();
        wrapper.groupBy("class_name");
        wrapper.lambda().eq(NftClass::getNftGroupId, groupId);
        List<NftClass> list = super.list(wrapper);
        return list;
    }

    @Override
    public List<NftClassDto> getClassByInfoId(Context context, String infoId) {
        QueryWrapper<NftClass> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftClass::getNftMetaId, infoId);
        List<NftClassDto> resultList = new ArrayList<>();
        List<NftClass> list = super.list(wrapper);

        changeLanguage(context, list);

        //构建Nftattribute
        list.forEach(e -> {
            List<NftAttribute> attributes = nftAttributeService.getByClassId(context, e.getId().toString());
            NftClassDto nftClassDto = NftClassDto.builder().id(e.getId())
                    .className(e.getClassName())
                    .nftGroupId(e.getNftGroupId())
                    .nftMetaId(e.getNftMetaId())
                    .score(e.getScore())
                    .isAttribute(e.getIsAttribute())
                    .attributes(attributes).build();
            resultList.add(nftClassDto);
        });
        return resultList;
    }

    @Override
    public Map<String, List<String>> getClassForMint(String infoId) {
        QueryWrapper<NftClass> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftClass::getNftMetaId, infoId);

        List<NftClass> list = super.list(wrapper);

        Map<String, List<String>> resultMap = new HashMap<>(3);
        List<String> types = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();

        list.forEach(e -> {
            if (e.getIsAttribute()) {
                List<NftAttribute> attributes = nftAttributeService.getByClassId(null, e.getId().toString());
                if (attributes != null && attributes.size() > 0) {
                    attributes.forEach(a -> {
                        if (keys.contains(Hex.encode(e.getClassName()))) {
                            return;
                        }
                        keys.add(Hex.encode(e.getClassName()));
                        values.add(Hex.encode(a.getValue()));
                        types.add(Hex.encode(a.getAttribute()));
                    });
                    keys.add(e.getClassName());
                    values.add(EMPTY);
                    types.add(Hex.encode(EMPTY));
                }
            } else {
                keys.add(Hex.encode(e.getClassName()));
                values.add(EMPTY);
                types.add(EMPTY);
            }
        });
        resultMap.put(NftPropertyType.KEYS.getType(), keys);
        resultMap.put(NftPropertyType.VALUES.getType(), values);
        resultMap.put(NftPropertyType.TYPES.getType(), types);
        return resultMap;
    }

    private void changeLanguage(Context context, List<NftClass> list) {
        Set setClassName = list.stream().map(e -> e.getClassName()).collect(Collectors.toSet());

        Map mapClassName = languageService.getByKeys(context, setClassName);

        list.forEach(e -> {
            e.setClassName(mapClassName.get(e.getClassName()).toString());
        });
    }
}
