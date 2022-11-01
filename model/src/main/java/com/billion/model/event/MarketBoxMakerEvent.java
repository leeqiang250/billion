package com.billion.model.event;

import com.alibaba.fastjson2.annotation.JSONField;
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
public class MarketBoxMakerEvent implements Serializable {

    public static String EVENT_NAME = "::secondary_market::BoxMakerEvent<";

    String id;

    String type;

    String maker;

    String price;

    String amount;

    String ts;

    @JSONField(name = "dead_ts")
    String deadTs;

    public void setType(String type) {
        if (type.startsWith("0x")) {
            this.type = Hex.decodeToString(type);
        } else {
            this.type = type;
        }
    }
}