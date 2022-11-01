package com.billion.model.event;

import com.aptos.request.v1.model.Event;

/**
 * @author liqiang
 */
public class EventType {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * isOpenBoxEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isOpenBoxEvent(Event event) {
        return OpenBoxEvent.EVENT_NAME.equals(event.getType());
    }

    public static boolean isNftComposeEvent(Event event) {
        return NftComposeEvent.EVENT_NAME.equals(event.getType());
    }

    public static boolean isNftSplitEvent(Event event) {
        return NftSplitEvent.EVENT_NAME.equals(event.getType());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}