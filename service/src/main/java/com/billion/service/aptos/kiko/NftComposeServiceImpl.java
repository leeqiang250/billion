package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftComposeMapper;
import com.billion.model.entity.NftCompose;
import com.billion.service.aptos.AbstractCacheService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liqiang
 */
@Service
public class NftComposeServiceImpl extends AbstractCacheService<NftComposeMapper, NftCompose> implements NftComposeService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeGe(Long version) {
        QueryWrapper<NftCompose> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().ge(NftCompose::getVersion, version);
        return super.remove(queryWrapper);
    }

}