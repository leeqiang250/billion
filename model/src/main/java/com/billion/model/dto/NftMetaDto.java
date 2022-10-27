package com.billion.model.dto;

import com.billion.model.entity.Token;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NftMetaDto {
    /**
     * id
     */
    Long id;

    /**
     * nft_group_id
     */
    Long nftGroupId;

    /**
     * 名称
     */
    String displayName;

    /**
     * 描述
     */
    String description;

    /**
     * 图片
     */
    String uri;

    /**
     * 排名
     */
    Long rank;

    /**
     * 销毁状态(0:未销毁;1:已销毁)
     */
    Boolean isBorn;

    /**
     * token_id
     */
    String tokenId;

    /**
     * 分数
     */
    String score;

    String creator;

    Integer attributeType;

    String owner;

    String contract;

    List<NftAttribute> attributeValues;

    String orderId;

    String saleType;

    String price;

    String bidder;

    Token bidToken;

    String auctionPrice;

    String bidPrice;

    String ts;

    String deadTs;
}
