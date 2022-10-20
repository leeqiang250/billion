package com.billion.service.aptos.kiko;

import com.billion.model.event.NftComposeEvent;
import com.billion.model.event.NftSplitEvent;

/**
 * @author liqiang
 */
public interface NftOpService {

    /**
     * addNftSplitEvent
     *
     * @param nftSplitEvent nftSplitEvent
     * @return boolean
     */
    boolean addNftSplitEvent(NftSplitEvent nftSplitEvent);

    /**
     * addNftComposeEvent
     *
     * @param nftComposeEvent nftComposeEvent
     * @return boolean
     */
    boolean addNftComposeEvent(NftComposeEvent nftComposeEvent);

}