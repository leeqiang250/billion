package com.billion.service.aptos.kiko;

import com.billion.model.entity.NftTransfer;
import com.billion.model.service.ICacheService;

/**
 * @author liqiang
 */
public interface NftTransferService extends ICacheService<NftTransfer> {

    /**
     * dispatch
     *
     * @return NftTransfer
     */
    NftTransfer dispatch();

}