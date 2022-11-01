package com.billion.model.event;

import com.alibaba.fastjson2.annotation.JSONField;
import com.aptos.request.v1.model.Event;
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
public class MarketBoxTakerEvent implements Serializable {

    public static String EVENT_NAME = "::secondary_market::BoxTakerEvent<";

    String id;

    String type;

    String maker;

    String price;

    String amount;

    String ts;

    @JSONField(name = "dead_ts")
    String deadTs;

    String bidder;

    @JSONField(name = "final_price")
    String finalPrice;

    @JSONField(name = "creator_fee")
    String creatorFee;

    @JSONField(name = "platform_fee")
    String platformFee;

    public void setType(String type) {
        if (type.startsWith("0x")) {
            this.type = Hex.decodeToString(type);
        } else {
            this.type = type;
        }
    }

    /**
     * isMarketBoxTakerEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isMarketBoxTakerEvent(Event event) {
        return event.getType().contains(MarketBoxTakerEvent.EVENT_NAME);
    }

}