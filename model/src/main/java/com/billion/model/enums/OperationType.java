package com.billion.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperationType {

    NFT_MINT_EVENT("NFTMintEvent", "NFT铸造事件"),
    NFT_MAKER_EVENT("NFTMakerEvent", "NFT铸造事件"),
    NFT_TAKER_EVENT("NFTtakerEvent", "NFT铸造事件"),
    NFT_BID_EVENT("NFTBidEvent", "NFT铸造事件"),
    NFT_CANCLE_EVENT("NFTCancleEvent", "NFT铸造事件"),
    NFT_ACCEPT_BID_EVENT("NFTAcceptBidEvent", "NFT铸造事件"),
    NFT_OFFLINE_EVENT("NFTOfflineEvent", "NFT铸造事件"),
    NFT_CHANGE_PRICE_EVENT("NFTChangePriceEvent", "NFT铸造事件"),
    BOX_MAKER_EVENT("BoxMakerEvent", "NFT铸造事件"),
    BOX_TAKER_EVENT("BoxTakerEvent", "NFT铸造事件"),
    BOX_BID_EVENT("BoxBidEvent", "NFT铸造事件"),
    BOX_CANCLE_EVENT("BoxCancleEvent", "NFT铸造事件"),

    ;

    private String type;
    private String desc;

}