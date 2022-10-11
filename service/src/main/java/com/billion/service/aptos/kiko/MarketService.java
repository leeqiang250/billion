package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Event;
import com.aptos.request.v1.model.Transaction;
import com.billion.model.entity.Market;
import com.billion.model.event.BoxBidEvent;
import com.billion.model.event.BoxCancelEvent;
import com.billion.model.event.BoxMakerEvent;
import com.billion.model.event.BoxTakerEvent;
import com.billion.model.service.ICacheService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liqiang
 */
public interface MarketService extends ICacheService<Market> {

    /**
     * isBoxMakerEvent
     *
     * @param event event
     * @return boolean
     */
    boolean isBoxMakerEvent(Event event);

    /**
     * isBoxTakerEvent
     *
     * @param event event
     * @return boolean
     */
    boolean isBoxTakerEvent(Event event);

    /**
     * isBoxBidEvent
     *
     * @param event event
     * @return boolean
     */
    boolean isBoxBidEvent(Event event);

    /**
     * isBoxCancelEvent
     *
     * @param event event
     * @return boolean
     */
    boolean isBoxCancelEvent(Event event);

    /**
     * addBoxMakerEvent
     *
     * @param transaction   transaction
     * @param event         event
     * @param boxMakerEvent boxMakerEvent
     * @return Market
     */
    @Transactional(rollbackFor = Exception.class)
    Market addBoxMakerEvent(Transaction transaction, Event event, BoxMakerEvent boxMakerEvent);

    /**
     * addBoxTakerEvent
     *
     * @param transaction   transaction
     * @param event         event
     * @param boxTakerEvent boxTakerEvent
     * @return Market
     */
    @Transactional(rollbackFor = Exception.class)
    Market addBoxTakerEvent(Transaction transaction, Event event, BoxTakerEvent boxTakerEvent);

    /**
     * addBoxBidEvent
     *
     * @param transaction transaction
     * @param event       event
     * @param boxBidEvent boxBidEvent
     * @return Market
     */
    @Transactional(rollbackFor = Exception.class)
    Market addBoxBidEvent(Transaction transaction, Event event, BoxBidEvent boxBidEvent);

    /**
     * addBoxCancelEvent
     *
     * @param transaction    transaction
     * @param event          event
     * @param boxCancelEvent boxCancelEvent
     * @return Market
     */
    @Transactional(rollbackFor = Exception.class)
    Market addBoxCancelEvent(Transaction transaction, Event event, BoxCancelEvent boxCancelEvent);

}