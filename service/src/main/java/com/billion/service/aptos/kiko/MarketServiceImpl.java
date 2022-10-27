package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Event;
import com.aptos.request.v1.model.Transaction;
import com.aptos.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.billion.dao.aptos.kiko.MarketMapper;
import com.billion.model.dto.Context;
import com.billion.model.dto.MarketDto;
import com.billion.model.entity.*;
import com.billion.model.enums.*;
import com.billion.model.enums.TransactionStatus;
import com.billion.model.event.*;
import com.billion.service.aptos.AbstractCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.billion.model.constant.RequestPath.EMPTY;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class MarketServiceImpl extends AbstractCacheService<MarketMapper, Market> implements MarketService {

    @Resource
    @Lazy
    NftMetaService nftMetaService;

    @Resource
    TokenService tokenService;

    @Resource
    OperationService operationService;

    @Resource
    BoxGroupService boxGroupService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeGe(Long version) {
        QueryWrapper<Market> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Market::getChain, Chain.APTOS.getCode());
        queryWrapper.lambda().ge(Market::getVersion, version);
        return super.remove(queryWrapper);
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
                .bidAmount(EMPTY)
                .bidder(EMPTY)
                .ts(boxMakerEvent.getTs())
                .deadTs(boxMakerEvent.getDeadTs())
                .isEnabled(Boolean.TRUE)
                .build();
        market.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_2_ING);
        market.setTransactionHash(transaction.getHash());
        super.save(market);

        //交易记录,售卖
        operationService.addBoxMakerOpt(transaction, event, boxMakerEvent);

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
        market.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_2_ING);
        market.setTransactionHash(transaction.getHash());
        super.save(market);

        //交易记录，购买
        operationService.addBoxTakerOpt(transaction, event, boxTakerEvent);

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
        market.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_2_ING);
        market.setTransactionHash(transaction.getHash());
        super.save(market);

        //交易记录, 拍卖出价
        operationService.addBoxBidOpt(transaction, event, boxBidEvent);

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
        market.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_2_ING);
        market.setTransactionHash(transaction.getHash());
        super.save(market);

        //交易记录,取消交易
        operationService.addBoxCancelOpt(transaction, event, boxCancelEvent);

        return market;
    }

    @Override
    public Market addNftMakerEvent(Transaction transaction, Event event, MarketMakerEvent nftMakerEvent) {
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
                .bidAmount(EMPTY)
                .bidder(EMPTY)
                .tokenId(nftMakerEvent.getTokenId().getNftTokenIdKey())
                .ts(nftMakerEvent.getTs())
                .deadTs(nftMakerEvent.getDeadTs())
                .isEnabled(Boolean.TRUE)
                .build();
        market.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_2_ING);
        market.setTransactionHash(transaction.getHash());
        super.save(market);

        //交易记录,售卖
        operationService.addNftMakerOpt(transaction, event, nftMakerEvent);

        return market;
    }

    @Override
    public Market addNftTakerEvent(Transaction transaction, Event event, MarketTakerEvent nftTakerEvent) {
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
        market.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_2_ING);
        market.setTransactionHash(transaction.getHash());
        super.save(market);

        //交易记录，购买
        operationService.addNftTakerOpt(transaction, event, nftTakerEvent);

        return market;
    }

    @Override
    public Market addNftBidEvent(Transaction transaction, Event event, MarketBidEvent nftBidEvent) {
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
        market.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_2_ING);
        market.setTransactionHash(transaction.getHash());
        super.save(market);

        //交易记录,拍卖出价
        operationService.addNftBidOpt(transaction, event, nftBidEvent);

        return market;

    }

    @Override
    public Market addNftCancelEvent(Transaction transaction, Event event, MarketCancelEvent nftCancelEvent) {
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
        market.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_2_ING);
        market.setTransactionHash(transaction.getHash());
        super.save(market);

        //交易记录，取消交易
        operationService.addNftCancelOpt(transaction, event, nftCancelEvent);

        return market;
    }

    @Override
    public MarketDto getMarketList(Context context, String condition, String order, String orderType, Integer pageStart, Integer pageLimt) {
        QueryWrapper<Market> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Market::getChain, context.getChain());
//        List status = List.of(com.billion.model.enums.TransactionStatus.STATUS_1_READY.getCode(), com.billion.model.enums.TransactionStatus.STATUS_2_ING.getCode());
//        queryWrapper.lambda().in(Market::getTransactionStatus, status);
        queryWrapper.lambda().eq(Market::getIsEnabled, Boolean.TRUE);

        if (MarketTokenType.NFT.getType().equals(condition)) {
            queryWrapper.lambda().ne(Market::getTokenId, EMPTY);
        }else if (MarketTokenType.BOX.getType().equals(condition)) {
            queryWrapper.lambda().eq(Market::getTokenId, EMPTY);
        }

        Page<Market> page = Page.of(pageStart, pageLimt);
        if (("asc").equals(orderType)) {
            page.addOrder(OrderItem.asc(order));
        }else {
            page.addOrder(OrderItem.desc(order));
        }

        var pageResult = this.page(page, queryWrapper);

        var marketList = pageResult.getRecords();
        //过滤orderid重复的market记录
        var marketMap = marketList.stream().collect(Collectors.toMap(market -> market.getOrderId(), (market -> market), (key1, key2) -> key2));
        var distinctMarkets = marketMap.values().stream().collect(Collectors.toList());

        List<String> nftTokenIdList = distinctMarkets.stream().filter(market -> StringUtils.isNotEmpty(market.getTokenId())).map(market -> market.getTokenId()).collect(Collectors.toList());
        List<String> coinIdList = distinctMarkets.stream().filter(market -> StringUtils.isEmpty(market.getTokenId())).map(market -> market.getAskToken()).collect(Collectors.toList());

        var nftTokenList = nftMetaService.getListByTokenIds(nftTokenIdList);
        var coinList = tokenService.getByCoinIdList(context, coinIdList);

        var nftMap = nftTokenList.stream().collect(Collectors.toMap(e -> e.getTokenId(), (e) -> e));
        var coinMap = coinList.stream().collect(Collectors.toMap(e -> e.getModuleAddress() + "::"
                + e.getModuleName() + "::" + e.getStructName(), (e) -> e));

        List<MarketDto.MarketInfo> resultList = new ArrayList<>();
        distinctMarkets.forEach(e -> {
            MarketDto.MarketInfo marketInfoDto = MarketDto.MarketInfo.builder()
                    .id(e.getId())
                    .chain(e.getChain())
                    .orderId(e.getOrderId())
                    .saleType(e.getType())
                    .price(e.getPrice())
                    .owner(e.getMaker())
                    .askAmount(e.getAskAmount())
                    .bidder(e.getBidder())
                    .bidAmount(e.getBidAmount())
                    .ts(e.getTs())
                    .deadTs(e.getDeadTs()).build();
            if (StringUtils.isNotEmpty(e.getBidToken())) {
                String[] tokenInfo = e.getBidToken().split("::");
                marketInfoDto.setBidToken(tokenService.getByTokenInfo(context, tokenInfo[0], tokenInfo[1], tokenInfo[2]));
            }
            if (StringUtils.isEmpty(e.getTokenId())) {
                marketInfoDto.setAskToken(coinMap.get(e.getAskToken()));
                BoxGroup boxGroup = boxGroupService.getByTokenId(context, e.getAskToken());
                marketInfoDto.setOrderType(MarketTokenType.BOX.getType());
                marketInfoDto.setName(boxGroup.getDisplayName());
                marketInfoDto.setContract(e.getAskToken());
                marketInfoDto.setUri(boxGroup.getUri());
            } else {
                marketInfoDto.setAskToken(nftMap.get(e.getTokenId()));
                marketInfoDto.setOrderType(MarketTokenType.NFT.getType());
                NftMeta nftMeta = nftMap.get(e.getTokenId());
                marketInfoDto.setName(nftMeta.getDisplayName());
                marketInfoDto.setContract(nftMetaService.getContract(context, nftMeta));
                marketInfoDto.setUri(nftMeta.getUri());
                marketInfoDto.setScore(nftMeta.getScore());
            }
            resultList.add(marketInfoDto);
        });
        MarketDto marketDto = MarketDto.builder()
                .pages(pageResult.getPages())
                .total(pageResult.getTotal())
                .currentPage(pageResult.getCurrent())
                .marketList(resultList)
                .build();
        return marketDto;
    }

    @Override
    public List<Market> getMarketListByAccount(Context context, String account, String type) {
        QueryWrapper<Market> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Market::getChain, context.getChain());
        List status = List.of(com.billion.model.enums.TransactionStatus.STATUS_1_READY.getCode(), com.billion.model.enums.TransactionStatus.STATUS_2_ING.getCode());
        queryWrapper.lambda().in(Market::getTransactionStatus, status);
        //指定发起交易的账户
        queryWrapper.lambda().eq(Market::getMaker, account);
        queryWrapper.lambda().orderByAsc(Market::getId);

        if (MarketTokenType.NFT.getType().equals(type)) {
            queryWrapper.lambda().ne(Market::getTokenId, EMPTY);
        }else if (MarketTokenType.BOX.getType().equals(type)) {
            queryWrapper.lambda().eq(Market::getTokenId, EMPTY);
        }
        var marketList = this.list(queryWrapper);
        var marketMap = marketList.stream().collect(Collectors.toMap(market -> market.getOrderId(), (market -> market), (key1, key2) -> key2));

        return marketMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public List<Market> getMarketListByTokenId(Context context, String tokenId) {
        QueryWrapper<Market> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Market::getChain, context.getChain());
        List status = List.of(com.billion.model.enums.TransactionStatus.STATUS_1_READY.getCode(), com.billion.model.enums.TransactionStatus.STATUS_2_ING.getCode());
        queryWrapper.lambda().in(Market::getTransactionStatus, status);
        //指定发起交易的账户
        queryWrapper.lambda().eq(Market::getTokenId, tokenId);
        queryWrapper.lambda().orderByAsc(Market::getId);

        return this.list(queryWrapper);
    }

    @Override
    public List<Market> getMarketListByOrderId(Context context, String orderId) {
        QueryWrapper<Market> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Market::getChain, context.getChain());
        List status = List.of(com.billion.model.enums.TransactionStatus.STATUS_1_READY.getCode(), com.billion.model.enums.TransactionStatus.STATUS_2_ING.getCode());
        queryWrapper.lambda().in(Market::getTransactionStatus, status);
        //指定发起交易的账户
        queryWrapper.lambda().eq(Market::getOrderId, orderId);
        queryWrapper.lambda().orderByAsc(Market::getId);

        return this.list(queryWrapper);
    }

}