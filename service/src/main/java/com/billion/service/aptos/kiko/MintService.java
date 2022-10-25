package com.billion.service.aptos.kiko;

import java.io.Serializable;

/**
 * @author liqiang
 */
public interface MintService {

    /**
     * initialize
     *
     * @param boxGroupId boxGroupId
     * @return boolean
     */
    boolean initialize(Serializable boxGroupId);


    /**
     * generateNftMetaFile
     *
     * @param nftMetaId nftMetaId
     * @return boolean
     */
    boolean generateNftMetaFile(long nftMetaId);
}