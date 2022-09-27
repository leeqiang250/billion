package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.billion.dao.aptos.kiko.NftAttributeMapper;
import com.billion.dao.aptos.kiko.NftClassMapper;
import com.billion.model.dto.Context;
import com.billion.model.dto.NFTClassDto;
import com.billion.model.entity.NftAttribute;
import com.billion.model.entity.NftClass;
import com.billion.service.aptos.AbstractCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
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
        wrapper.lambda().eq(NftClass::getNftGroupId, groupId);
//        super.
        List<NftClass> list = super.list(wrapper);
        return list;
    }

    @Override
    public List getClassByInfoId(Context context, String infoId) {
        QueryWrapper<NftClass> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftClass::getNftInfoId, infoId);

        List<NFTClassDto> resultList = new ArrayList<>();
        List<NftClass> list = super.list(wrapper);
        //构建Nftattribute
        list.stream().forEach(e -> {
            List<NftAttribute> attributes = nftAttributeService.getByClassId(e.getId().toString());
            NFTClassDto nftClassDto = NFTClassDto.builder().id(e.getId())
                    .nftGroupId(e.getNftGroupId())
                    .nftInfoId(e.getNftInfoId())
                    .score(e.getScore())
                    .type(e.getType())
                    .attributes(attributes).build();
            resultList.add(nftClassDto);
        });
        return list;
    }
}
