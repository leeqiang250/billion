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
        return event.getType().contains(BoxMakerEvent.EVENT_NAME);
    }

    /**
     * isMarketBoxTakerEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isMarketBoxTakerEvent(Event event) {
        return event.getType().contains(BoxTakerEvent.EVENT_NAME);
    }

    /**
     * isMarketBoxBidEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isMarketBoxBidEvent(Event event) {
        return event.getType().contains(BoxBidEvent.EVENT_NAME);
    }

    /**
     * isMarketBoxCancelEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isMarketBoxCancelEvent(Event event) {
        return event.getType().contains(BoxCancelEvent.EVENT_NAME);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * isMarketNftMakerEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isMarketNftMakerEvent(Event event) {
        return event.getType().contains(MarketMakerEvent.EVENT_NAME);
    }

    /**
     * isMarketNftTakerEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isMarketNftTakerEvent(Event event) {
        return event.getType().contains(MarketTakerEvent.EVENT_NAME);
    }

    /**
     * isMarketNftBidEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isMarketNftBidEvent(Event event) {
        return event.getType().contains(MarketBidEvent.EVENT_NAME);
    }

    /**
     * isMarketNftCancelEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isMarketNftCancelEvent(Event event) {
        return event.getType().contains(MarketCancelEvent.EVENT_NAME);
    }

}