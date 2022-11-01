package com.billion.model.event;

import com.aptos.request.v1.model.Event;

/**
 * @author liqiang
 */
public class EventMarketNft {

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