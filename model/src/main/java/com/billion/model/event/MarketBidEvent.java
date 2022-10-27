package com.billion.model.event;

import com.alibaba.fastjson2.annotation.JSONField;
import com.aptos.request.v1.model.TokenId;
import com.aptos.utils.Hex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liqiang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketBidEvent implements Serializable {
    
    public static String EVENT_NAME = "::secondary_market::NftBidEvent<";

    String id;

    String type;

    String maker;

    String price;

    @JSONField(name = "token_id")
    TokenId tokenId;

    String amount;

    String ts;

    @JSONField(name = "dead_ts")
    String deadTs;

    String bidder;

    @JSONField(name = "bid_price")
    String bidPrice;

    @JSONField(name = "prev_bidder")
    String prevBidder;

    @JSONField(name = "prev_bid_price")
    String prevBidPrice;

    public void setType(String type) {
        if (type.startsWith("0x")) {
            this.type = Hex.decodeToString(type);
        } else {
            this.type = type;
        }
    }
}
