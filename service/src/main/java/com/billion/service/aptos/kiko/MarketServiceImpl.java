package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Event;
import com.aptos.request.v1.model.Response;
import com.aptos.request.v1.model.TableTokenData;
import com.aptos.request.v1.model.Transaction;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.MarketMapper;
import com.billion.model.dto.Context;
import com.billion.model.entity.*;
import com.billion.model.enums.Chain;
import com.billion.model.enums.TradeStatus;
import com.billion.model.event.*;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class MarketServiceImpl extends AbstractCacheService<MarketMapper, Market> implements MarketService {
    @Resource
    BoxGroupService boxGroupService;

    @Resource
    NftMetaService nftMetaService;

    @Resource
    TokenService tokenService;

    @Resource
    LanguageService languageService;

    @Resource
    AptosService aptosService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeGe(Long version) {
        QueryWrapper<Market> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Market::getChain, Chain.APTOS.getCode());
        queryWrapper.lambda().ge(Market::getVersion, version);
        return super.remove(queryWrapper);
    }

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

        super.save(market);

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

        super.save(market);

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
                .askToken(nftBidEvent.getTokenId().getTokenDataId().getNftGroupKey())
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

        super.save(market);

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
                .tokenId(nftCancelEvent.getTokenId().getNftTokenIdKey())
                .ts(nftCancelEvent.getTs())
                .deadTs(nftCancelEvent.getDeadTs())
                .isEnabled(Boolean.TRUE)
                .build();

        market.setTradeStatus_(TradeStatus.STATUS_2_CANCEL);

        super.save(market);

        //TODO 如有需要，交易记录

        return market;
    }

    @Override
    public List<Market> getMarketList(Context context) {
        QueryWrapper<Market> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Market::getChain, context.getChain());
        //TODO:没写完
//        queryWrapper.lambda().in()
        return this.list(queryWrapper);
    }



    public List<Market> getMarketList(Context context, String id, String type) {
        String tokenId = "";
        if ("box".equals(type)) {
            BoxGroup boxGroup = boxGroupService.getById(id);
            Token token = tokenService.getById(boxGroup.getAskToken());
            tokenId = token.getModuleAddress() + "::" + token.getModuleName() + "::" +  token.getStructName();
        }else if ("nft".equals(type)) {
            NftMeta nftMeta = nftMetaService.cacheById(context, id);
            QueryWrapper<Language> languageQueryWrapper = new QueryWrapper<>();
            languageQueryWrapper.lambda().eq(Language::getLanguage, com.billion.model.enums.Language.EN.getCode());
            languageQueryWrapper.lambda().eq(Language::getKey, nftMeta.getDisplayName());
            var displayName = languageService.getOneThrowEx(languageQueryWrapper).getValue();
            //通过链上查询tokenId
            Response<TableTokenData> tableTokenDataResponse =  AptosService.getAptosClient().requestTableTokenData(nftMeta.getTableHandle(), nftMeta.getTableCreator(),
                    nftMeta.getTableCollection(), displayName);
            tokenId = (String)List.of(nftMeta.getTableCreator(), nftMeta.getTableCollection(), displayName,
                    tableTokenDataResponse.getData().getLargestPropertyVersion()).stream().collect(Collectors.joining("@"));

        }
        return this.getMarketListById(context, tokenId, type);

    }


    public List<Market> getMarketListById(Context context, String tokenId, String type) {
        QueryWrapper<Market> marketQueryWrapper = new QueryWrapper<>();
        marketQueryWrapper.lambda().eq(Market::getChain, context.getChain());
        if ("box".equals(type)) {
            marketQueryWrapper.lambda().eq(Market::getAskToken, tokenId);
        }else if ("nft".equals(type)) {
            marketQueryWrapper.lambda().eq(Market::getTokenId, tokenId);
        }
        return this.list(marketQueryWrapper);
    }

}