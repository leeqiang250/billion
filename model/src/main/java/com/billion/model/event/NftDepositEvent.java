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
public class NftDepositEvent implements Serializable {

    public static final String EVENT_NAME = "0x3::token::DepositEvent";

    String amount;

    TokenId id;

    /**
     * isDepositEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isNftDepositEvent(Event event) {
        return NftDepositEvent.EVENT_NAME.equals(event.getType());
    }

}