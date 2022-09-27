package com.billion.service.aptos.kiko;

import com.billion.model.entity.TokenTransfer;
import com.billion.model.service.ICacheService;

/**
 * @author liqiang
 */
public interface TokenTransferService extends ICacheService<TokenTransfer> {

    /**
     * transfer
     *
     * @return TokenTransfer
     */
    TokenTransfer transfer();

}