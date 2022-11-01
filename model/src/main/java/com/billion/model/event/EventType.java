package com.billion.model.event;

import com.aptos.request.v1.model.Event;

/**
 * @author liqiang
 */
public class EventType {

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