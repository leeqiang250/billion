package com.billion.model.event;

import com.aptos.request.v1.model.Event;

/**
 * @author liqiang
 */
public class EventTypeNft {

    /**
     * isNftCreateTokenDataEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isNftCreateTokenDataEvent(Event event) {
        return NftCreateTokenDataEvent.EVENT_NAME.equals(event.getType());
    }

    /**
     * isWithdrawEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isNftWithdrawEvent(Event event) {
        return NftWithdrawEvent.EVENT_NAME.equals(event.getType());
    }

    /**
     * isDepositEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isNftDepositEvent(Event event) {
        return NftDepositEvent.EVENT_NAME.equals(event.getType());
    }

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