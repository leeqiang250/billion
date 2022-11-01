package com.billion.model.event;

import com.aptos.request.v1.model.Event;

/**
 * @author liqiang
 */
public class EventTypeMarket {

    /**
     * isMarketBoxMakerEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isMarketBoxMakerEvent(Event event) {
        return event.getType().contains(MarketBoxMakerEvent.EVENT_NAME);
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

    /**
     * isMarketBoxBidEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isMarketBoxBidEvent(Event event) {
        return event.getType().contains(MarketBoxBidEvent.EVENT_NAME);
    }

    /**
     * isMarketBoxCancelEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isMarketBoxCancelEvent(Event event) {
        return event.getType().contains(MarketBoxCancelEvent.EVENT_NAME);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * isMarketNftMakerEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isMarketNftMakerEvent(Event event) {
        return event.getType().contains(MarketNftMakerEvent.EVENT_NAME);
    }

    /**
     * isMarketNftTakerEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isMarketNftTakerEvent(Event event) {
        return event.getType().contains(MarketNftTakerEvent.EVENT_NAME);
    }

    /**
     * isMarketNftBidEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isMarketNftBidEvent(Event event) {
        return event.getType().contains(MarketNftBidEvent.EVENT_NAME);
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