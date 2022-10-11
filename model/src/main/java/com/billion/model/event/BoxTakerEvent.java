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
public class BoxTakerEvent implements Serializable {

    String id;

    String type;

    String maker;

    String price;

    String amount;

    String ts;

    String taker;

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

}