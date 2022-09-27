package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftClassMapper;
import com.billion.model.dto.Context;
import com.billion.model.dto.NftClassDto;
import com.billion.model.entity.NftAttribute;
import com.billion.model.entity.NftClass;
import com.billion.service.aptos.AbstractCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author jason
 */
@Service
public class NftClassServiceImpl extends AbstractCacheService<NftClassMapper, NftClass> implements NftClassService {
    @Autowired
    NftAttributeService nftAttributeService;


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
        wrapper.lambda().eq(NftClass::getNftInfoId, infoId);

        List<NftClassDto> resultList = new ArrayList<>();
        List<NftClass> list = super.list(wrapper);
        //构建Nftattribute
        list.stream().forEach(e -> {
            List<NftAttribute> attributes = nftAttributeService.getByClassId(e.getId().toString());
            NftClassDto nftClassDto = NftClassDto.builder().id(e.getId())
                    .nftGroupId(e.getNftGroupId())
                    .nftInfoId(e.getNftInfoId())
                    .score(e.getScore())
                    .type(e.getType())
                    .attributes(attributes).build();
            resultList.add(nftClassDto);
        });
        return resultList;
    }
}
