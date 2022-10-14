package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Event;
import com.aptos.request.v1.model.Transaction;
import com.billion.model.dto.Context;
import com.billion.model.entity.Market;
import com.billion.model.event.*;
import com.billion.model.service.ICacheService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author liqiang
 */
public interface MarketService extends ICacheService<Market> {

    /**
     * removeGe
     *
     * @param version version
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    boolean removeGe(Long version);

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * isNftMakerEvent
     *
     * @param event event
     * @return boolean
     */
    boolean isNftMakerEvent(Event event);

    /**
     * isNftTakerEvent
     *
     * @param event event
     * @return boolean
     */
    boolean isNftTakerEvent(Event event);

    /**
     * isNftBidEvent
     *
     * @param event event
     * @return boolean
     */
    boolean isNftBidEvent(Event event);

    /**
     * isNftCancelEvent
     *
     * @param event event
     * @return boolean
     */
    boolean isNftCancelEvent(Event event);

    /**
     * addNftMakerEvent
     *
     * @param transaction   transaction
     * @param event         event
     * @param nftMakerEvent nftMakerEvent
     * @return Market
     */
    @Transactional(rollbackFor = Exception.class)
    Market addNftMakerEvent(Transaction transaction, Event event, NftMakerEvent nftMakerEvent);

    /**
     * addNftTakerEvent
     *
     * @param transaction   transaction
     * @param event         event
     * @param nftTakerEvent nftTakerEvent
     * @return Market
     */
    @Transactional(rollbackFor = Exception.class)
    Market addNftTakerEvent(Transaction transaction, Event event, NftTakerEvent nftTakerEvent);

    /**
     * addNftBidEvent
     *
     * @param transaction transaction
     * @param event       event
     * @param nftBidEvent nftBidEvent
     * @return Market
     */
    @Transactional(rollbackFor = Exception.class)
    Market addNftBidEvent(Transaction transaction, Event event, NftBidEvent nftBidEvent);

    /**
     * addNftCancelEvent
     *
     * @param transaction    transaction
     * @param event          event
     * @param nftCancelEvent nftCancelEvent
     * @return Market
     */
    @Transactional(rollbackFor = Exception.class)
    Market addNftCancelEvent(Transaction transaction, Event event, NftCancelEvent nftCancelEvent);


    List<Market> getMarketList(Context context);
}