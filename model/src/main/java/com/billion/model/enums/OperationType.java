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
    NFT_WITHDRAW_EVENT("NFTWithDrawEvent", ""),
    NFT_DEPOSIT_EVENT("NFTDepositEvent", ""),
    NFT_BURN_EVENT("NFTBurnEvent", ""),
    NFT_MAKER_EVENT("NFTMakerEvent", ""),
    NFT_TAKER_EVENT("NFTTakerEvent", ""),
    NFT_BID_EVENT("NFTBidEvent", ""),
    NFT_CANCLE_EVENT("NFTCancelEvent", ""),
    BOX_MAKER_EVENT("BoxMakerEvent", ""),
    BOX_TAKER_EVENT("BoxTakerEvent", ""),
    BOX_BID_EVENT("BoxBidEvent", ""),
    BOX_CANCLE_EVENT("BoxCancelEvent", ""),
    BOX_OPEN_EVENT("BoxOpenEvent", ""),
    ;

    String type;
    String desc;

}