package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Event;
import com.aptos.request.v1.model.Transaction;
import com.billion.dao.aptos.kiko.MarketMapper;
import com.billion.model.entity.Market;
import com.billion.model.enums.Chain;
import com.billion.model.enums.TradeStatus;
import com.billion.model.event.*;
import com.billion.service.aptos.AbstractCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liqiang
 */

@Slf4j
@Service
public class MarketServiceImpl extends AbstractCacheService<MarketMapper, Market> implements MarketService {

    @Override
    public boolean isBoxMakerEvent(Event event) {
        return event.getType().contains("::secondary_market::BoxMakerEvent<");
    }

    @Override
    public boolean isBoxTakerEvent(Event event) {
        return event.getType().contains("::secondary_market::BoxTakerEvent<");
    }

    @Override
    public boolean isBoxBidEvent(Event event) {
        return event.getType().contains("::secondary_market::BoxBidEvent<");
    }

    @Override
    public boolean isBoxCancelEvent(Event event) {
        return event.getType().contains("::secondary_market::BoxCancelEvent<");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Market addBoxMakerEvent(Transaction transaction, Event event, BoxMakerEvent boxMakerEvent) {
        Market market = Market.builder()
                .chain(Chain.APTOS.getCode())
                .version(Long.parseLong(transaction.getVersion()))
                .orderId(boxMakerEvent.getId())
                .type(boxMakerEvent.getType())
                .maker(boxMakerEvent.getMaker())
                .price(boxMakerEvent.getPrice())
                .askToken(event.getType().split("<")[1].split(">")[0].split(",")[0].trim())
                .askAmount(boxMakerEvent.getAmount())
                .bidToken(event.getType().split("<")[1].split(">")[0].split(",")[1].trim())
                .bidAmount("")
                .bidder("")
                .ts(boxMakerEvent.getTs())
                .deadTs(boxMakerEvent.getDeadTs())
                .isEnabled(Boolean.TRUE)
                .build();

        market.setTradeStatus_(TradeStatus.STATUS_0_BIDDING);

        super.save(market);

        //TODO 如有需要，交易记录

        return market;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Market addBoxTakerEvent(Transaction transaction, Event event, BoxTakerEvent boxTakerEvent) {
        Market market = Market.builder()
                .chain(Chain.APTOS.getCode())
                .version(Long.parseLong(transaction.getVersion()))
                .orderId(boxTakerEvent.getId())
                .type(boxTakerEvent.getType())
                .maker(boxTakerEvent.getMaker())
                .price(boxTakerEvent.getPrice())
                .askToken(event.getType().split("<")[1].split(">")[0].split(",")[0].trim())
                .askAmount(boxTakerEvent.getAmount())
                .bidToken(event.getType().split("<")[1].split(">")[0].split(",")[1].trim())
                .bidAmount(boxTakerEvent.getFinalPrice())
                .bidder(boxTakerEvent.getBidder())
                .ts(boxTakerEvent.getTs())
                .deadTs(boxTakerEvent.getDeadTs())
                .isEnabled(Boolean.TRUE)
                .build();

        market.setTradeStatus_(TradeStatus.STATUS_1_COMPLETE);

        super.save(market);

        //TODO 如有需要，交易记录

        return market;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Market addBoxBidEvent(Transaction transaction, Event event, BoxBidEvent boxBidEvent) {
        Market market = Market.builder()
                .chain(Chain.APTOS.getCode())
                .version(Long.parseLong(transaction.getVersion()))
                .orderId(boxBidEvent.getId())
                .type(boxBidEvent.getType())
                .maker(boxBidEvent.getMaker())
                .price(boxBidEvent.getPrice())
                .askToken(event.getType().split("<")[1].split(">")[0].split(",")[0].trim())
                .askAmount(boxBidEvent.getAmount())
                .bidToken(event.getType().split("<")[1].split(">")[0].split(",")[1].trim())
                .bidAmount(boxBidEvent.getBidPrice())
                .bidder(boxBidEvent.getBidder())
                .ts(boxBidEvent.getTs())
                .deadTs(boxBidEvent.getDeadTs())
                .isEnabled(Boolean.TRUE)
                .build();

        market.setTradeStatus_(TradeStatus.STATUS_0_BIDDING);

        super.save(market);

        //TODO 如有需要，交易记录

        return market;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Market addBoxCancelEvent(Transaction transaction, Event event, BoxCancelEvent boxCancelEvent) {
        Market market = Market.builder()
                .chain(Chain.APTOS.getCode())
                .version(Long.parseLong(transaction.getVersion()))
                .orderId(boxCancelEvent.getId())
                .type(boxCancelEvent.getType())
                .maker(boxCancelEvent.getMaker())
                .price(boxCancelEvent.getPrice())
                .askToken(event.getType().split("<")[1].split(">")[0].split(",")[0].trim())
                .askAmount(boxCancelEvent.getAmount())
                .bidToken(event.getType().split("<")[1].split(">")[0].split(",")[1].trim())
                .bidAmount(boxCancelEvent.getBidPrice())
                .bidder(boxCancelEvent.getBidder())
                .ts(boxCancelEvent.getTs())
                .deadTs(boxCancelEvent.getDeadTs())
                .isEnabled(Boolean.TRUE)
                .build();

        market.setTradeStatus_(TradeStatus.STATUS_2_CANCEL);

        super.save(market);

        //TODO 如有需要，交易记录

        return market;
    }

    @Override
    public boolean isNftMakerEvent(Event event) {
        return false;
    }

    @Override
    public boolean isNftTakerEvent(Event event) {
        return false;
    }

    @Override
    public boolean isNftBidEvent(Event event) {
        return false;
    }

    @Override
    public boolean isNftCancelEvent(Event event) {
        return false;
    }

    @Override
    public Market addNftMakerEvent(Transaction transaction, Event event, NftMakerEvent nftMakerEvent) {
        return null;
    }

    @Override
    public Market addNftTakerEvent(Transaction transaction, Event event, NftTakerEvent nftTakerEvent) {
        return null;
    }

    @Override
    public Market addNftBidEvent(Transaction transaction, Event event, NftBidEvent nftBidEvent) {
        return null;
    }

    @Override
    public Market addNftCancelEvent(Transaction transaction, Event event, NftCancelEvent nftCancelEvent) {
        return null;
    }


}