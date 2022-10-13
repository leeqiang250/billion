package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Event;
import com.aptos.request.v1.model.Transaction;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

        this.add(market);

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

        this.add(market);

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

        this.add(market);

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

        this.add(market);

        //TODO 如有需要，交易记录

        return market;
    }

    @Override
    public boolean isNftMakerEvent(Event event) {
        return event.getType().contains("::secondary_market::NftMakerEvent<");
    }

    @Override
    public boolean isNftTakerEvent(Event event) {
        return event.getType().contains("::secondary_market::NftTakerEvent<");
    }

    @Override
    public boolean isNftBidEvent(Event event) {
        return event.getType().contains("::secondary_market::NftBidEvent<");
    }

    @Override
    public boolean isNftCancelEvent(Event event) {
        return event.getType().contains("::secondary_market::NftCancelEvent<");
    }

    @Override
    public Market addNftMakerEvent(Transaction transaction, Event event, NftMakerEvent nftMakerEvent) {
        Market market = Market.builder()
                .chain(Chain.APTOS.getCode())
                .version(Long.parseLong(transaction.getVersion()))
                .orderId(nftMakerEvent.getId())
                .type(nftMakerEvent.getType())
                .maker(nftMakerEvent.getMaker())
                .price(nftMakerEvent.getPrice())
                .askToken(nftMakerEvent.getTokenId().getTokenDataId().getNftGroupKey())
                .askAmount(nftMakerEvent.getAmount())
                .bidToken(event.getType().split("<")[1].split(">")[0].split(",")[0].trim())
                .bidAmount("")
                .bidder("")
                .tokenId(nftMakerEvent.getTokenId().getNftTokenIdKey())
                .ts(nftMakerEvent.getTs())
                .deadTs(nftMakerEvent.getDeadTs())
                .isEnabled(Boolean.TRUE)
                .build();

        market.setTradeStatus_(TradeStatus.STATUS_0_BIDDING);

        this.add(market);

        //TODO 如有需要，交易记录

        return market;
    }

    @Override
    public Market addNftTakerEvent(Transaction transaction, Event event, NftTakerEvent nftTakerEvent) {
        Market market = Market.builder()
                .chain(Chain.APTOS.getCode())
                .version(Long.parseLong(transaction.getVersion()))
                .orderId(nftTakerEvent.getId())
                .type(nftTakerEvent.getType())
                .maker(nftTakerEvent.getMaker())
                .price(nftTakerEvent.getPrice())
                .askToken(nftTakerEvent.getTokenId().getTokenDataId().getNftGroupKey())
                .askAmount(nftTakerEvent.getAmount())
                .bidToken(event.getType().split("<")[1].split(">")[0].split(",")[0].trim())
                .bidAmount(nftTakerEvent.getFinalPrice())
                .bidder(nftTakerEvent.getBidder())
                .tokenId(nftTakerEvent.getTokenId().getNftTokenIdKey())
                .ts(nftTakerEvent.getTs())
                .deadTs(nftTakerEvent.getDeadTs())
                .isEnabled(Boolean.TRUE)
                .build();

        market.setTradeStatus_(TradeStatus.STATUS_1_COMPLETE);

        this.add(market);

        //TODO 如有需要，交易记录

        return market;
    }

    @Override
    public Market addNftBidEvent(Transaction transaction, Event event, NftBidEvent nftBidEvent) {
        Market market = Market.builder()
                .chain(Chain.APTOS.getCode())
                .version(Long.parseLong(transaction.getVersion()))
                .orderId(nftBidEvent.getId())
                .type(nftBidEvent.getType())
                .maker(nftBidEvent.getMaker())
                .price(nftBidEvent.getPrice())
                .askToken(nftTakerEvent.getTokenId().getTokenDataId().getNftGroupKey())
                .askAmount(nftBidEvent.getAmount())
                .bidToken(event.getType().split("<")[1].split(">")[0].split(",")[0].trim())
                .bidAmount(nftBidEvent.getBidPrice())
                .bidder(nftBidEvent.getBidder())
                .tokenId(nftBidEvent.getTokenId().getNftTokenIdKey())
                .ts(nftBidEvent.getTs())
                .deadTs(nftBidEvent.getDeadTs())
                .isEnabled(Boolean.TRUE)
                .build();

        market.setTradeStatus_(TradeStatus.STATUS_0_BIDDING);

        this.add(market);

        //TODO 如有需要，交易记录

        return market;

    }

    @Override
    public Market addNftCancelEvent(Transaction transaction, Event event, NftCancelEvent nftCancelEvent) {
        Market market = Market.builder()
                .chain(Chain.APTOS.getCode())
                .version(Long.parseLong(transaction.getVersion()))
                .orderId(nftCancelEvent.getId())
                .type(nftCancelEvent.getType())
                .maker(nftCancelEvent.getMaker())
                .price(nftCancelEvent.getPrice())
                .askToken(nftCancelEvent.getTokenId().getTokenDataId().getNftGroupKey())
                .askAmount(nftCancelEvent.getAmount())
                .bidToken(event.getType().split("<")[1].split(">")[0].split(",")[0].trim())
                .bidAmount(nftCancelEvent.getBidPrice())
                .bidder(nftCancelEvent.getBidder())
                .tokenId(nftBidEvent.getTokenId().getNftTokenIdKey())
                .ts(nftCancelEvent.getTs())
                .deadTs(nftCancelEvent.getDeadTs())
                .isEnabled(Boolean.TRUE)
                .build();

        market.setTradeStatus_(TradeStatus.STATUS_2_CANCEL);

        this.add(market);

        //TODO 如有需要，交易记录

        return market;
    }

    void add(Market market) {
        QueryWrapper<Market> marketQueryWrapper = new QueryWrapper<>();
        marketQueryWrapper.lambda().eq(Market::getChain, Chain.APTOS.getCode());
        marketQueryWrapper.lambda().ge(Market::getVersion, market.getVersion());
        super.remove(marketQueryWrapper);
        super.save(market);
    }

}