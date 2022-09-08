package com.billion.model.constant;

/**
 * @author liqiang
 */
public interface RequestPathConstant {
    String APTOS = "/aptos";
    String APP = APTOS + "/kiko";
    String V1_VERSION = APP + "/v1";

    /**
     * common路径下无需鉴权
     */
    String V1_COMMON = V1_VERSION + "/common";
    /**
     * common路径下无需鉴权
     */
    String V1_IMAGE = V1_COMMON + "/image";
    /**
     * common路径下无需鉴权
     */
    String V1_LANGUAGE = V1_COMMON + "/language";
    /**
     * common路径下无需鉴权
     */
    String V1_ERROR = V1_COMMON + "/error";
    /**
     * common路径下无需鉴权
     */
    String V1_NFT = V1_VERSION + "/nft";
    String V1_LIQUIDITY = V1_VERSION + "/liquidity";
    String V1_SWAP = V1_VERSION + "/swap";
    String V1_META = V1_VERSION + "/meta";
    String V1_FARM = V1_VERSION + "/farm";
    String V1_LOAN = V1_VERSION + "/loan";

}