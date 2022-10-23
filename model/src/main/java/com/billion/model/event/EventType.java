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

    /**
     * isBoxMakerEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isBoxMakerEvent(Event event) {
        return event.getType().contains(BoxMakerEvent.EVENT_NAME);
    }

    /**
     * isBoxTakerEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isBoxTakerEvent(Event event) {
        return event.getType().contains(BoxTakerEvent.EVENT_NAME);
    }

    /**
     * isBoxBidEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isBoxBidEvent(Event event) {
        return event.getType().contains(BoxBidEvent.EVENT_NAME);
    }

    /**
     * isBoxCancelEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isBoxCancelEvent(Event event) {
        return event.getType().contains(BoxCancelEvent.EVENT_NAME);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * isNftMakerEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isNftMakerEvent(Event event) {
        return event.getType().contains("::secondary_market::NftMakerEvent<");
    }

    /**
     * isNftTakerEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isNftTakerEvent(Event event) {
        return event.getType().contains("::secondary_market::NftTakerEvent<");
    }

    /**
     * isNftBidEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isNftBidEvent(Event event) {
        return event.getType().contains("::secondary_market::NftBidEvent<");
    }

    /**
     * isNftCancelEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isNftCancelEvent(Event event) {
        return event.getType().contains("::secondary_market::NftCancelEvent<");
    }

}