package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftSplitMapper;
import com.billion.model.entity.NftSplit;
import com.billion.service.aptos.AbstractCacheService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liqiang
 */
@Service
public class NftSplitServiceImpl extends AbstractCacheService<NftSplitMapper, NftSplit> implements NftSplitService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeGe(Long version) {
        QueryWrapper<NftSplit> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().ge(NftSplit::getVersion, version);
        return super.remove(queryWrapper);
    }

}