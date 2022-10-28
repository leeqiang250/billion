package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftAttributeMetaMapper;
import com.billion.model.entity.NftAttributeMeta;
import com.billion.service.aptos.AbstractCacheService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author jason
 */
@Service
public class NftAttributeMetaServiceImpl extends AbstractCacheService<NftAttributeMetaMapper, NftAttributeMeta> implements NftAttributeMetaService {

    @Override
    public List<NftAttributeMeta> getNftAttributeMetaByNftGroupId(Collection<Long> nftAttributeTypeIds) {
        var wrapper = new QueryWrapper<NftAttributeMeta>();
        wrapper.lambda().in(NftAttributeMeta::getNftAttributeTypeId, nftAttributeTypeIds);
        wrapper.lambda().orderByAsc(NftAttributeMeta::getId);
        return super.list(wrapper);
    }

}