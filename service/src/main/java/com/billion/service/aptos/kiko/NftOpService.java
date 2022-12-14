package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Transaction;
import com.billion.model.event.OpNftComposeEvent;
import com.billion.model.event.OpNftSplitEvent;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liqiang
 */
public interface NftOpService {

    /**
     * addNftSplitEvent
     *
     * @param transaction   transaction
     * @param nftSplitEvent nftSplitEvent
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    boolean addNftSplitEvent(Transaction transaction, OpNftSplitEvent nftSplitEvent);

    /**
     * addNftComposeEvent
     *
     * @param transaction     transaction
     * @param nftComposeEvent nftComposeEvent
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    boolean addNftComposeEvent(Transaction transaction, OpNftComposeEvent nftComposeEvent);

    /**
     * execute
     *
     * @return boolean
     */
    boolean execute();

    /**
     * initialize
     *
     * @return boolean
     */
    boolean initialize();

}