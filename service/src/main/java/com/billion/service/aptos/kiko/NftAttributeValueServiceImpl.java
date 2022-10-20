package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.billion.dao.aptos.kiko.NftAttributeValueMapper;
import com.billion.model.dto.NftAttribute;
import com.billion.model.entity.NftAttributeValue;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jason
 */
@Service
public class NftAttributeValueServiceImpl extends ServiceImpl<NftAttributeValueMapper, NftAttributeValue> implements NftAttributeValueService {

    @Resource
    NftAttributeTypeService nftAttributeTypeService;

    @Override
    public List<NftAttribute> getNftAttributeForMint(Long nftMetaId) {
        QueryWrapper<NftAttributeValue> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(NftAttributeValue::getNftMetaId, nftMetaId);
        var attributeValue = super.list(queryWrapper);

        List<NftAttribute> resultList = new ArrayList<>();
        attributeValue.forEach(v -> {
            var attributeType = nftAttributeTypeService.getNftAttributeInfoByMetaId(v.getNftAttributeMetaId());
            resultList.add(attributeType);
        });

        return resultList;
    }
}