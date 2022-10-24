package com.billion.service.aptos.kiko;

import com.billion.model.entity.NftSplit;
import com.billion.model.service.ICacheService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liqiang
 */
public interface NftSplitService extends ICacheService<NftSplit> {

    /**
     * removeGe
     *
     * @param version version
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    boolean removeGe(Long version);

}