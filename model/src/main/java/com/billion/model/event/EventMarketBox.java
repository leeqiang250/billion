package com.billion.model.event;

import com.aptos.request.v1.model.Event;

/**
 * @author liqiang
 */
public class EventMarketBox {

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

}