package com.billion.model.constant;

import java.util.List;

/**
 * @author liqiang
 */
public interface RequestPathConstant {

    String PING = "/ping";

    String APTOS = "/aptos";

    String APP = APTOS + "/kiko";

    String V1 = APP + "/v1";

    /**
     * common路径下无需鉴权
     */
    String V1_COMMON = V1 + "/common";

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
    String V1_CONTRACT = V1_COMMON + "/contract";

    String V1_NFT = V1 + "/nft";

    String V1_LIQUIDITY = V1 + "/liquidity";

    String V1_SWAP = V1 + "/swap";

    String V1_META = V1 + "/meta";

    String V1_FARM = V1 + "/farm";

    String V1_LOAN = V1 + "/loan";

    String[] WHITE = List.of("/", PING, V1_COMMON).toArray(String[]::new);

}