package com.billion.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqiang
 */

@Getter
@AllArgsConstructor
public enum OperationType {

    NFT_MINT_EVENT("NFTMintEvent", ""),
    NFT_MAKER_EVENT("NFTMakerEvent", ""),
    NFT_TAKER_EVENT("NFTTakerEvent", ""),
    NFT_BID_EVENT("NFTBidEvent", ""),
    NFT_CANCLE_EVENT("NFTCancelEvent", ""),
    NFT_ACCEPT_BID_EVENT("NFTAcceptBidEvent", ""),
    NFT_OFFLINE_EVENT("NFTOfflineEvent", ""),
    NFT_CHANGE_PRICE_EVENT("NFTChangePriceEvent", ""),
    BOX_MAKER_EVENT("BoxMakerEvent", ""),
    BOX_TAKER_EVENT("BoxTakerEvent", ""),
    BOX_BID_EVENT("BoxBidEvent", ""),
    BOX_CANCLE_EVENT("BoxCancelEvent", ""),
    ;

    String type;
    String desc;

}