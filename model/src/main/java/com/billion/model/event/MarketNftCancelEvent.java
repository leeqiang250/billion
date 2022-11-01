package com.billion.model.event;

import com.alibaba.fastjson2.annotation.JSONField;
import com.aptos.request.v1.model.Event;
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
public class MarketNftCancelEvent implements Serializable {

    public static String EVENT_NAME = "::secondary_market::NftCancelEvent<";

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

    public void setType(String type) {
        if (type.startsWith("0x")) {
            this.type = Hex.decodeToString(type);
        } else {
            this.type = type;
        }
    }

    /**
     * isMarketNftCancelEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isMarketNftCancelEvent(Event event) {
        return event.getType().contains(MarketNftCancelEvent.EVENT_NAME);
    }

}
