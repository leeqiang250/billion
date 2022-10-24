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
import com.billion.model.enums.Chain;
import com.billion.model.enums.MarketTokenType;
import com.billion.model.enums.OperationType;
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

        //交易记录
        Operation operation = Operation.builder()
                .chain(Chain.APTOS.getCode())
                .owner(boxMakerEvent.getMaker())
                .type(OperationType.BOX_MAKER_EVENT.getType())
                .tokenId(event.getType().split("<")[1].split(">")[0].split(",")[0].trim())
                .tokenAmount(Long.valueOf(boxMakerEvent.getAmount()))
                .bidToken(EMPTY)
                .price(0L)
                .build();
        operation.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        operation.setTransactionHash(transaction.getHash());
        operationService.save(operation);

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

        //交易记录
        Operation operation = Operation.builder()
                .chain(Chain.APTOS.getCode())
                .owner(boxTakerEvent.getBidder())
                .type(OperationType.BOX_TAKER_EVENT.getType())
                .tokenId(event.getType().split("<")[1].split(">")[0].split(",")[0].trim())
                .tokenAmount(Long.valueOf(boxTakerEvent.getAmount()))
                .bidToken(event.getType().split("<")[1].split(">")[0].split(",")[1].trim())
                .price(Long.valueOf(boxTakerEvent.getFinalPrice()))
                .build();
        operation.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        operation.setTransactionHash(transaction.getHash());
        operationService.save(operation);

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

        //交易记录
        Operation operation = Operation.builder()
                .chain(Chain.APTOS.getCode())
                .owner(boxBidEvent.getBidder())
                .type(OperationType.BOX_BID_EVENT.getType())
                .tokenId(event.getType().split("<")[1].split(">")[0].split(",")[0].trim())
                .tokenAmount(Long.valueOf(boxBidEvent.getAmount()))
                .bidToken(event.getType().split("<")[1].split(">")[0].split(",")[1].trim())
                .price(Long.valueOf(boxBidEvent.getBidPrice()))
                .build();
        operation.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        operation.setTransactionHash(transaction.getHash());
        operationService.save(operation);

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

        //交易记录
        Operation operation = Operation.builder()
                .chain(Chain.APTOS.getCode())
                .owner(boxCancelEvent.getMaker())
                .type(OperationType.BOX_CANCLE_EVENT.getType())
                .tokenId(event.getType().split("<")[1].split(">")[0].split(",")[0].trim())
                .tokenAmount(Long.valueOf(boxCancelEvent.getAmount()))
                .bidToken(EMPTY)
                .price(0L)
                .build();
        operation.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        operation.setTransactionHash(transaction.getHash());
        operationService.save(operation);

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

        //交易记录
        Operation operation = Operation.builder()
                .chain(Chain.APTOS.getCode())
                .owner(nftMakerEvent.getMaker())
                .type(OperationType.NFT_MAKER_EVENT.getType())
                .tokenId(nftMakerEvent.getTokenId().getNftTokenIdKey())
                .tokenAmount(1L)
                .bidToken(event.getType().split("<")[1].split(">")[0].split(",")[0].trim())
                .price(0L)
                .build();
        operation.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        operation.setTransactionHash(transaction.getHash());
        operationService.save(operation);

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

        //交易记录
        Operation operation = Operation.builder()
                .chain(Chain.APTOS.getCode())
                .owner(nftTakerEvent.getBidder())
                .type(OperationType.NFT_TAKER_EVENT.getType())
                .tokenId(nftTakerEvent.getTokenId().getNftTokenIdKey())
                .tokenAmount(Long.valueOf(nftTakerEvent.getAmount()))
                .bidToken(event.getType().split("<")[1].split(">")[0].split(",")[0].trim())
                .price(Long.valueOf(nftTakerEvent.getFinalPrice()))
                .build();
        operation.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        operation.setTransactionHash(transaction.getHash());
        operationService.save(operation);

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

        //交易记录
        Operation operation = Operation.builder()
                .chain(Chain.APTOS.getCode())
                .owner(nftBidEvent.getMaker())
                .type(OperationType.NFT_BID_EVENT.getType())
                .tokenId(nftBidEvent.getTokenId().getNftTokenIdKey())
                .tokenAmount(Long.valueOf(nftBidEvent.getAmount()))
                .bidToken(event.getType().split("<")[1].split(">")[0].split(",")[0].trim())
                .price(Long.valueOf(nftBidEvent.getBidPrice()))
                .build();
        operation.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        operation.setTransactionHash(transaction.getHash());
        operationService.save(operation);

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

        //交易记录
        Operation operation = Operation.builder()
                .chain(Chain.APTOS.getCode())
                .owner(nftCancelEvent.getMaker())
                .type(OperationType.NFT_CANCLE_EVENT.getType())
                .tokenId(nftCancelEvent.getTokenId().getNftTokenIdKey())
                .tokenAmount(Long.valueOf(nftCancelEvent.getAmount()))
                .bidToken(EMPTY)
                .price(0L)
                .build();
        operation.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        operation.setTransactionHash(transaction.getHash());
        operationService.save(operation);

        return market;
    }

    @Override
    public MarketDto getMarketList(Context context, String condition, String order, String orderType, Integer pageStart, Integer pageLimt) {
        QueryWrapper<Market> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Market::getChain, context.getChain());
        List status = List.of(com.billion.model.enums.TransactionStatus.STATUS_1_READY.getCode(), com.billion.model.enums.TransactionStatus.STATUS_2_ING.getCode());
        queryWrapper.lambda().in(Market::getTransactionStatus, status);

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
        List<String> nftTokenIdList = marketList.stream().filter(market -> StringUtils.isNotEmpty(market.getTokenId())).map(market -> market.getTokenId()).collect(Collectors.toList());
        List<String> coinIdList = marketList.stream().filter(market -> StringUtils.isEmpty(market.getTokenId())).map(market -> market.getAskToken()).collect(Collectors.toList());

        var nftTokenList = nftMetaService.getListByTokenIds(nftTokenIdList);
        var coinList = tokenService.getByCoinIdList(context, coinIdList);

        var nftMap = nftTokenList.stream().collect(Collectors.toMap(e -> e.getTokenId(), (e) -> e));
        var coinMap = coinList.stream().collect(Collectors.toMap(e -> e.getModuleAddress() + "::"
                + e.getModuleName() + "::" + e.getStructName(), (e) -> e));

        List<MarketDto.MarketInfo> resultList = new ArrayList<>();
        marketList.forEach(e -> {
            MarketDto.MarketInfo marketDto = MarketDto.MarketInfo.builder()
                    .id(e.getId())
                    .chain(e.getChain())
                    .orderId(e.getOrderId())
                    .type(e.getType())
                    .price(e.getPrice())
                    .maker(e.getMaker())
                    .askAmount(e.getAskAmount())
                    .bidder(e.getBidder())
                    .bidToken(e.getBidToken())
                    .bidAmount(e.getBidAmount())
                    .ts(e.getTs())
                    .deadTs(e.getDeadTs()).build();

            if (StringUtils.isEmpty(e.getTokenId())) {
                marketDto.setAskToken(coinMap.get(e.getAskToken()));
                marketDto.setOrderType(0);
            } else {
                marketDto.setAskToken(nftMap.get(e.getTokenId()));
                marketDto.setOrderType(1);
            }
            resultList.add(marketDto);
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

        if (MarketTokenType.NFT.getType().equals(type)) {
            queryWrapper.lambda().ne(Market::getTokenId, EMPTY);
        }else if (MarketTokenType.BOX.getType().equals(type)) {
            queryWrapper.lambda().eq(Market::getTokenId, EMPTY);
        }

        return this.list(queryWrapper);
    }

}