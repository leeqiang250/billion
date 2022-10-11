package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Event;
import com.aptos.request.v1.model.Transaction;
import com.billion.model.entity.Market;
import com.billion.model.event.BoxMakerEvent;
import com.billion.model.service.ICacheService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liqiang
 */
public interface MarketService extends ICacheService<Market> {

    /**
     * isAddBoxEvent
     *
     * @param event event
     * @return boolean
     */
    boolean isAddBoxEvent(Event event);

    /**
     * addBox
     *
     * @param transaction   transaction
     * @param event         event
     * @param boxMakerEvent boxMakerEvent
     * @return Market
     */
    @Transactional(rollbackFor = Exception.class)
    Market addBox(Transaction transaction, Event event, BoxMakerEvent boxMakerEvent);

}