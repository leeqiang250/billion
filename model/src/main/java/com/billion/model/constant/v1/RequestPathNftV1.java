package com.billion.model.constant.v1;

import static com.billion.model.constant.RequestPath.V1;

/**
 * @author liqiang
 */
public interface RequestPathNftV1 {

    String NFT = V1 + "/nft";

    String NFT_GROUP = NFT + "/group";

    String BOX_GROUP = NFT + "/boxGroup";

    String NFT_META = NFT + "/meta";

    String NFT_ATTRIBUTE = NFT + "/attribute";

    String NFT_CLASS = NFT + "/class";

}