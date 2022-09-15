package com.billion.model.constant;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author liqiang
 */
public interface RequestPathConstant {

    String PING = "/ping";

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

    String[] WHITE = List.of("/", PING, V1_COMMON).toArray(String[]::new);

}