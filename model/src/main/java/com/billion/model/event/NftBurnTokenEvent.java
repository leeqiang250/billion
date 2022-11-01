package com.billion.model.event;

import com.aptos.request.v1.model.Event;
import com.aptos.request.v1.model.TokenId;
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
public class NftBurnTokenEvent implements Serializable {

    public static final String EVENT_NAME = "0x3::token::BurnTokenEvent";

    String amount;

    TokenId id;

    /**
     * isNftBurnTokenEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isNftBurnTokenEvent(Event event) {
        return NftBurnTokenEvent.EVENT_NAME.equals(event.getType());
    }

}