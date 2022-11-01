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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public Market addBoxMakerEvent(Transaction transaction, Event event, MarketBoxMakerEvent boxMakerEvent) {
        Market market = Market.builder()
                .chain(Chain.APTOS.getCode())
                .version(Long.parseLong(transaction.getVersion()))
                .orderId(boxMakerEvent.getId())
                .type(boxMakerEvent.getType())
                .event(MarketBoxMakerEvent.EVENT_NAME)
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
        operationService.addMarketBoxMakerOpt(transaction, event, boxMakerEvent);

        return market;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Market addBoxTakerEvent(Transaction transaction, Event event, MarketBoxTakerEvent boxTakerEvent) {
        Market market = Market.builder()
                .chain(Chain.APTOS.getCode())
                .version(Long.parseLong(transaction.getVersion()))
                .orderId(boxTakerEvent.getId())
                .type(boxTakerEvent.getType())
                .event(MarketBoxTakerEvent.EVENT_NAME)
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
        operationService.addMarketBoxTakerOpt(transaction, event, boxTakerEvent);

        return market;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Market addBoxBidEvent(Transaction transaction, Event event, MarketBoxBidEvent boxBidEvent) {
        Market market = Market.builder()
                .chain(Chain.APTOS.getCode())
                .version(Long.parseLong(transaction.getVersion()))
                .orderId(boxBidEvent.getId())
                .type(boxBidEvent.getType())
                .event(MarketBoxBidEvent.EVENT_NAME)
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
        operationService.addMarketBoxBidOpt(transaction, event, boxBidEvent);

        return market;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Market addBoxCancelEvent(Transaction transaction, Event event, MarketBoxCancelEvent boxCancelEvent) {
        Market market = Market.builder()
                .chain(Chain.APTOS.getCode())
                .version(Long.parseLong(transaction.getVersion()))
                .orderId(boxCancelEvent.getId())
                .type(boxCancelEvent.getType())
                .event(MarketBoxCancelEvent.EVENT_NAME)
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
        operationService.addMarketBoxCancelOpt(transaction, event, boxCancelEvent);

        return market;
    }

    @Override
    public Market addNftMakerEvent(Transaction transaction, Event event, MarketNftMakerEvent nftMakerEvent) {
        Market market = Market.builder()
                .chain(Chain.APTOS.getCode())
                .version(Long.parseLong(transaction.getVersion()))
                .orderId(nftMakerEvent.getId())
                .type(nftMakerEvent.getType())
                .event(MarketNftMakerEvent.EVENT_NAME)
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
        operationService.addMarketNftMakerOpt(transaction, event, nftMakerEvent);

        return market;
    }

    @Override
    public Market addNftTakerEvent(Transaction transaction, Event event, MarketNftTakerEvent nftTakerEvent) {
        Market market = Market.builder()
                .chain(Chain.APTOS.getCode())
                .version(Long.parseLong(transaction.getVersion()))
                .orderId(nftTakerEvent.getId())
                .type(nftTakerEvent.getType())
                .event(MarketNftTakerEvent.EVENT_NAME)
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
        operationService.addMarketNftTakerOpt(transaction, event, nftTakerEvent);

        return market;
    }

    @Override
    public Market addNftBidEvent(Transaction transaction, Event event, MarketNftBidEvent nftBidEvent) {
        Market market = Market.builder()
                .chain(Chain.APTOS.getCode())
                .version(Long.parseLong(transaction.getVersion()))
                .orderId(nftBidEvent.getId())
                .type(nftBidEvent.getType())
                .event(MarketNftBidEvent.EVENT_NAME)
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
        operationService.addMarketNftBidOpt(transaction, event, nftBidEvent);

        return market;

    }

    @Override
    public Market addNftCancelEvent(Transaction transaction, Event event, MarketNftCancelEvent nftCancelEvent) {
        Market market = Market.builder()
                .chain(Chain.APTOS.getCode())
                .version(Long.parseLong(transaction.getVersion()))
                .orderId(nftCancelEvent.getId())
                .type(nftCancelEvent.getType())
                .event(MarketNftBidEvent.EVENT_NAME)
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
        operationService.addMarketNftCancelOpt(transaction, event, nftCancelEvent);

        return market;
    }

    @Override
    public MarketDto getMarketList(Context context, String condition, String order, String orderType, Integer pageStart, Integer pageLimt) {
        QueryWrapper<Market> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Market::getChain, context.getChain());
        List status = List.of(TransactionStatus.STATUS_3_SUCCESS.getCode(), com.billion.model.enums.TransactionStatus.STATUS_2_ING.getCode());
        queryWrapper.lambda().in(Market::getTransactionStatus, status);

        queryWrapper.lambda().eq(Market::getIsEnabled, Boolean.TRUE);

        if (MarketTokenType.NFT.getType().equals(condition)) {
            queryWrapper.lambda().ne(Market::getTokenId, EMPTY);
        } else if (MarketTokenType.BOX.getType().equals(condition)) {
            queryWrapper.lambda().eq(Market::getTokenId, EMPTY);
        }

        Page<Market> page = Page.of(pageStart, pageLimt);
        page.addOrder(OrderItem.desc("version"));
        if (("asc").equals(orderType)) {
            page.addOrder(OrderItem.asc(order));
        } else {
            page.addOrder(OrderItem.desc(order));
        }

        var pageResult = this.page(page, queryWrapper);

        var marketList = pageResult.getRecords();
        //去重数据
        marketList = distinctOnSalList(context, marketList);

        List<String> nftTokenIdList = marketList.stream().filter(market -> StringUtils.isNotEmpty(market.getTokenId())).map(market -> market.getTokenId()).collect(Collectors.toList());
        List<String> coinIdList = marketList.stream().filter(market -> StringUtils.isEmpty(market.getTokenId())).map(market -> market.getAskToken()).collect(Collectors.toList());

        var nftTokenList = nftMetaService.getListByTokenIds(nftTokenIdList);
        var coinList = tokenService.getByCoinIdList(context, coinIdList);

        var nftMap = nftTokenList.stream().collect(Collectors.toMap(e -> e.getTokenId(), (e) -> e));
        var coinMap = coinList.stream().collect(Collectors.toMap(e -> e.getModuleAddress() + "::"
                + e.getModuleName() + "::" + e.getStructName(), (e) -> e));

        List<MarketDto.MarketInfo> resultList = new ArrayList<>();
        marketList.forEach(e -> {
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
                marketInfoDto.setBoxGroupId(boxGroup.getId().toString());
            } else {
                marketInfoDto.setAskToken(nftMap.get(e.getTokenId()));
                marketInfoDto.setOrderType(MarketTokenType.NFT.getType());
                NftMeta nftMeta = nftMap.get(e.getTokenId());
                marketInfoDto.setName(nftMeta.getDisplayName());
                marketInfoDto.setContract(nftMetaService.getContract(context, nftMeta));
                marketInfoDto.setUri(nftMeta.getUri());
                marketInfoDto.setScore(nftMeta.getScore());
                marketInfoDto.setNftMetaId(nftMeta.getId().toString());
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
        List status = List.of(TransactionStatus.STATUS_3_SUCCESS.getCode(), com.billion.model.enums.TransactionStatus.STATUS_2_ING.getCode());
        queryWrapper.lambda().in(Market::getTransactionStatus, status);
        //指定发起交易的账户
        queryWrapper.lambda().eq(Market::getMaker, account);
        queryWrapper.lambda().eq(Market::getIsEnabled, Boolean.TRUE);
        queryWrapper.lambda().orderByDesc(Market::getVersion);

        if (MarketTokenType.NFT.getType().equals(type)) {
            queryWrapper.lambda().ne(Market::getTokenId, EMPTY);
        } else if (MarketTokenType.BOX.getType().equals(type)) {
            queryWrapper.lambda().eq(Market::getTokenId, EMPTY);
        }
        var marketList = this.list(queryWrapper);

        return distinctOnSalList(context, marketList);
    }

    @Override
    public Market getMarketByTokenId(Context context, String tokenId) {
        QueryWrapper<Market> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Market::getChain, context.getChain());
        List status = List.of(TransactionStatus.STATUS_3_SUCCESS.getCode(), com.billion.model.enums.TransactionStatus.STATUS_2_ING.getCode());
        queryWrapper.lambda().in(Market::getTransactionStatus, status);
        //指定发起交易的账户
        queryWrapper.lambda().eq(Market::getTokenId, tokenId);
        queryWrapper.lambda().eq(Market::getIsEnabled, Boolean.TRUE);
        queryWrapper.lambda().orderByDesc(Market::getVersion);

        var marketList = this.list(queryWrapper);
        Map<String, Market> makerMap = new HashMap<>(30);
        Map<String, Market> takerMap = new HashMap<>(30);
        Map<String, Market> cancleMap = new HashMap<>(30);
        Map<String, Market> bidMap = new HashMap<>(30);
        marketList.forEach(market -> {
            if (market.getEvent().equals(MarketBoxMakerEvent.EVENT_NAME) || market.getEvent().equals(MarketNftMakerEvent.EVENT_NAME)) {
                makerMap.put(market.getOrderId(), market);
            }
            if (market.getEvent().equals(MarketBoxTakerEvent.EVENT_NAME) || market.getEvent().equals(MarketNftTakerEvent.EVENT_NAME)) {
                takerMap.put(market.getOrderId(), market);
            }
            if (market.getEvent().equals(MarketBoxCancelEvent.EVENT_NAME) || market.getEvent().equals(MarketNftCancelEvent.EVENT_NAME)) {
                cancleMap.put(market.getOrderId(), market);
            }
            if (market.getEvent().equals(MarketBoxBidEvent.EVENT_NAME) || market.getEvent().equals(MarketNftBidEvent.EVENT_NAME)) {
                bidMap.put(market.getOrderId(), market);
            }
        });
        //根据orderId去掉已取消或已交易成功的
        var makerList = makerMap.values().stream().collect(Collectors.toList());
        makerList = makerList.stream().filter(market -> !takerMap.containsKey(market.getOrderId())).collect(Collectors.toList());
        makerList = makerList.stream().filter(market -> !cancleMap.containsKey(market.getOrderId())).collect(Collectors.toList());

        if (makerList.size() > 0) {
            Market market = marketList.get(0);
            if (bidMap.containsKey(market.getOrderId())) {
                market.setBidder(bidMap.get(market.getOrderId()).getBidder());
                market.setBidAmount(bidMap.get(market.getOrderId()).getBidAmount());
            }
            return market;
        }
        return null;
    }

    @Override
    public Market getMarketByOrderId(Context context, String orderId) {
        QueryWrapper<Market> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Market::getChain, context.getChain());
        List status = List.of(TransactionStatus.STATUS_3_SUCCESS.getCode(), com.billion.model.enums.TransactionStatus.STATUS_2_ING.getCode());
        queryWrapper.lambda().in(Market::getTransactionStatus, status);
        //指定orderId
        queryWrapper.lambda().eq(Market::getOrderId, orderId);
        queryWrapper.lambda().orderByDesc(Market::getVersion);
        var marketList = this.list(queryWrapper);
        if (marketList.size() == 0) {
            return null;
        }
        //修改拍卖最新报价
        Market resultMarket = null;
        String bidderAmount = null;
        String bidder = null;
        for (Market market : marketList) {
            if (market.getEvent().equals(MarketBoxCancelEvent.EVENT_NAME) || market.getEvent().equals(MarketBoxTakerEvent.EVENT_NAME)
                    || market.getEvent().equals(MarketNftCancelEvent.EVENT_NAME) || market.getEvent().equals(MarketNftTakerEvent.EVENT_NAME)) {
                return null;
            }
            if (market.getEvent().equals(MarketBoxMakerEvent.EVENT_NAME) || market.getEvent().equals(MarketNftMakerEvent.EVENT_NAME)) {
                resultMarket = market;
            }
            if (market.getEvent().equals(MarketBoxBidEvent.EVENT_NAME) || market.getEvent().equals(MarketNftBidEvent.EVENT_NAME)) {
                bidderAmount = market.getBidAmount();
            }
        }
        //说明有拍卖出价
        if (null != bidder && null != bidderAmount) {
            resultMarket.setBidder(bidder);
            resultMarket.setBidAmount(bidderAmount);
        }
        return resultMarket;
    }

    @Override
    public boolean isOnSale(Context context, String orderId) {
        QueryWrapper<Market> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Market::getChain, context.getChain());
        queryWrapper.lambda().eq(Market::getIsEnabled, Boolean.TRUE);
        queryWrapper.lambda().eq(Market::getOrderId, orderId);

        var marketList = this.list(queryWrapper);
        boolean onSale = true;
        for (var market : marketList)
            if (market.getEvent().equals(MarketBoxCancelEvent.EVENT_NAME) || market.getEvent().equals(MarketNftCancelEvent.EVENT_NAME)) {
                onSale = false;
                break;
            }

        return onSale;
    }

    private List<Market> distinctOnSalList(Context context, List<Market> marketList) {
        Map<String, Market> makerMap = new HashMap<>(30);
        Map<String, Market> takerMap = new HashMap<>(30);
        Map<String, Market> cancleMap = new HashMap<>(30);
        Map<String, Market> bidMap = new HashMap<>(30);
        marketList.forEach(market -> {
            if (market.getEvent().equals(MarketBoxMakerEvent.EVENT_NAME) || market.getEvent().equals(MarketNftMakerEvent.EVENT_NAME)) {
                makerMap.put(market.getOrderId(), market);
            }
            if (market.getEvent().equals(MarketBoxTakerEvent.EVENT_NAME) || market.getEvent().equals(MarketNftTakerEvent.EVENT_NAME)) {
                takerMap.put(market.getOrderId(), market);
            }
            if (market.getEvent().equals(MarketBoxCancelEvent.EVENT_NAME) || market.getEvent().equals(MarketNftCancelEvent.EVENT_NAME)) {
                cancleMap.put(market.getOrderId(), market);
            }
        });

        //根据orderId去掉已取消或已交易成功的
        var makerList = makerMap.values().stream().collect(Collectors.toList());
        makerList = makerList.stream().filter(market -> !takerMap.containsKey(market.getOrderId())).collect(Collectors.toList());
        makerList = makerList.stream().filter(market -> !cancleMap.containsKey(market.getOrderId())).collect(Collectors.toList());
        if (makerList.size() == 0) {
            return new ArrayList<>();
        }
        //取消或吃单事件有可能没在当前页查出来，根据orderId和event再去查一遍
        QueryWrapper<Market> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Market::getChain, context.getChain());
        queryWrapper.lambda().eq(Market::getIsEnabled, Boolean.TRUE);
        queryWrapper.lambda().in(Market::getOrderId, makerMap.keySet());
        queryWrapper.lambda().and(w1 -> {
            w1.or(w2 -> w2.eq(Market::getEvent, MarketNftCancelEvent.EVENT_NAME));
            w1.or(w2 -> w2.eq(Market::getEvent, MarketNftTakerEvent.EVENT_NAME));
            w1.or(w2 -> w2.eq(Market::getEvent, MarketBoxCancelEvent.EVENT_NAME));
            w1.or(w2 -> w2.eq(Market::getEvent, MarketBoxTakerEvent.EVENT_NAME));
        });
        var removeList = this.list(queryWrapper);
        var removeMap = removeList.stream().collect(Collectors.toMap(market -> market.getOrderId(), (market) -> market));
        makerList = makerList.stream().filter(market -> !removeMap.containsKey(market.getOrderId())).collect(Collectors.toList());
        if (makerList.size() == 0) {
            return new ArrayList<>();
        }

        //拍卖类型的订单，找出最高出价和出价人
        var orderIdList = makerList.stream().filter(market -> market.getType().equals("auction")).map(Market::getOrderId).collect(Collectors.toList());
        queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Market::getChain, context.getChain());
        queryWrapper.lambda().eq(Market::getOrderId, orderIdList);
        queryWrapper.lambda().eq(Market::getIsEnabled, Boolean.TRUE);
        queryWrapper.lambda().and(w1 -> {
            w1.or(w2 -> w2.eq(Market::getEvent, MarketNftMakerEvent.EVENT_NAME));
            w1.or(w2 -> w2.eq(Market::getEvent, MarketBoxMakerEvent.EVENT_NAME));
        });

        var higheBidList = this.list(queryWrapper);
        var highBidMap = higheBidList.stream().collect(Collectors.toMap(market -> market.getOrderId(), market -> market));

        makerList.forEach(market -> {
            if (highBidMap.containsKey(market.getOrderId())) {
                market.setBidder(highBidMap.get(market.getOrderId()).getBidder());
                market.setBidAmount(highBidMap.get(market.getOrderId()).getBidAmount());
            }
        });
        return makerList;
    }


}