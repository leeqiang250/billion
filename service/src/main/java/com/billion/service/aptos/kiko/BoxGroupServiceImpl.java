package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.CoinInfo;
import com.aptos.request.v1.model.CoinStore;
import com.aptos.request.v1.model.Response;
import com.aptos.request.v1.model.TransactionPayload;
import com.aptos.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.billion.dao.aptos.kiko.BoxGroupMapper;
import com.billion.model.dto.BoxGroupDto;
import com.billion.model.dto.Context;
import com.billion.model.dto.MyBoxDto;
import com.billion.model.entity.*;
import com.billion.model.enums.*;
import com.billion.model.enums.Language;
import com.billion.model.enums.TokenScene;
import com.billion.model.enums.TransactionStatus;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
import com.billion.service.aptos.ContextService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.billion.model.constant.RequestPath.EMPTY;

/**
 * @author liqiang
 */
@Service
public class BoxGroupServiceImpl extends AbstractCacheService<BoxGroupMapper, BoxGroup> implements BoxGroupService {

    @Resource
    TokenService tokenService;

    @Resource
    PairService pairService;

    @Resource
    @Lazy
    MarketService marketService;

    @Resource
    LanguageService languageService;

    @Override
    public Map cacheMap(Context context) {
        String key = this.cacheMapKey("ids::" + context.getChain());

        Map map = this.getRedisTemplate().opsForHash().entries(key);
        if (!map.isEmpty()) {
            return map;
        }

        QueryWrapper<BoxGroup> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BoxGroup::getChain, context.getChain());
        wrapper.lambda().eq(BoxGroup::getIsEnabled, Boolean.TRUE);
        List<BoxGroup> list = super.list(wrapper);

        changeLanguage(context, list);

        map = list.stream().collect(Collectors.toMap(e -> e.getId().toString(), (e) -> e));

        this.getRedisTemplate().opsForHash().putAll(key, map);
        this.getRedisTemplate().expire(key, this.cacheSecond(CacheTsType.MIDDLE));

        return map;
    }

    @Override
    public List<BoxGroup> cacheList(Context context) {
        Map map = this.cacheMap(context);
        List<BoxGroup> list = new ArrayList<>(map.values());
        return list;
    }

    @Override
    public boolean initialize(Serializable id) {
        QueryWrapper<BoxGroup> boxGroupQueryWrapper = new QueryWrapper<>();
        boxGroupQueryWrapper.lambda().eq(BoxGroup::getId, id);
        boxGroupQueryWrapper.lambda().eq(BoxGroup::getChain, Chain.APTOS.getCode());
        boxGroupQueryWrapper.lambda().eq(BoxGroup::getIsEnabled, Boolean.TRUE);
        boxGroupQueryWrapper.lambda().in(BoxGroup::getTransactionStatus, List.of(TransactionStatus.STATUS_1_READY.getCode(), TransactionStatus.STATUS_3_SUCCESS.getCode()));
        var boxGroup = super.getOneThrowEx(boxGroupQueryWrapper);
        if (TransactionStatus.STATUS_3_SUCCESS == boxGroup.getTransactionStatus_()) {
            return this.initializeMarket();
        }

        var askToken = tokenService.getById(boxGroup.getAskToken());
        var bidToken = tokenService.getById(boxGroup.getBidToken());

        if (Objects.isNull(askToken)
                || Objects.isNull(bidToken)
                || TransactionStatus.STATUS_3_SUCCESS != askToken.getTransactionStatus_()
                || TransactionStatus.STATUS_3_SUCCESS != bidToken.getTransactionStatus_()
        ) {
            boxGroup.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            boxGroup.setTransactionHash(EMPTY);
            super.updateById(boxGroup);

            return false;
        }

        com.aptos.request.v1.model.Resource askTokenResource = com.aptos.request.v1.model.Resource.builder()
                .moduleAddress(askToken.getModuleAddress())
                .moduleName(askToken.getModuleName())
                .resourceName(askToken.getStructName())
                .build();

        com.aptos.request.v1.model.Resource bidTokenResource = com.aptos.request.v1.model.Resource.builder()
                .moduleAddress(bidToken.getModuleAddress())
                .moduleName(bidToken.getModuleName())
                .resourceName(bidToken.getStructName())
                .build();

        TransactionPayload transactionPayload = TransactionPayload.builder()
                .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                .function(ContextService.getKikoOwner() + "::primary_market::add_box_bid")
                .arguments(List.of(
                        boxGroup.getAmount(),
                        boxGroup.getTs(),
                        boxGroup.getPrice()
                ))
                .typeArguments(List.of(askTokenResource.resourceTag(), bidTokenResource.resourceTag()))
                .build();

        var response = AptosService.getAptosClient().requestSubmitTransaction(
                ContextService.getKikoOwner(),
                transactionPayload);
        if (response.isValid()) {
            boxGroup.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            boxGroup.setTransactionHash(EMPTY);
            super.updateById(boxGroup);

            return false;
        }

        if (!AptosService.checkTransaction(response.getData().getHash())) {
            boxGroup.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            boxGroup.setTransactionHash(Objects.isNull(response.getData().getHash()) ? EMPTY : response.getData().getHash());
            super.updateById(boxGroup);

            return false;
        }

        boxGroup.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        boxGroup.setTransactionHash(response.getData().getHash());

        super.updateById(boxGroup);

        return this.initializeMarket();
    }

    @Override
    public boolean initializeMarket() {
        Context context = Context.builder()
                .chain(Chain.APTOS.getCode())
                .language(Language.EN.getCode())
                .build();

        var function = ContextService.getKikoOwner() + "::secondary_market::box_init";

        QueryWrapper<BoxGroup> boxGroupQueryWrapper = new QueryWrapper<>();
        boxGroupQueryWrapper.lambda().eq(BoxGroup::getChain, Chain.APTOS.getCode());
        boxGroupQueryWrapper.lambda().eq(BoxGroup::getIsEnabled, Boolean.TRUE);
        boxGroupQueryWrapper.lambda().eq(BoxGroup::getTransactionStatus, TransactionStatus.STATUS_3_SUCCESS.getCode());
        var boxGroups = super.list(boxGroupQueryWrapper);

        var tokens = tokenService.getByScene(context, TokenScene.MARKET.getCode());

        for (BoxGroup boxGroup : boxGroups) {
            var askToken = tokenService.getById(boxGroup.getAskToken());

            for (Token bidToken : tokens) {
                QueryWrapper<Pair> pairQueryWrapper = new QueryWrapper<>();
                pairQueryWrapper.lambda().eq(Pair::getContract, function);
                pairQueryWrapper.lambda().eq(Pair::getAskToken, askToken.getId());
                pairQueryWrapper.lambda().eq(Pair::getBidToken, bidToken.getId());
                var pair = this.pairService.getOne(pairQueryWrapper, false);
                if (Objects.isNull(pair)) {
                    com.aptos.request.v1.model.Resource askTokenResource = com.aptos.request.v1.model.Resource.builder()
                            .moduleAddress(askToken.getModuleAddress())
                            .moduleName(askToken.getModuleName())
                            .resourceName(askToken.getStructName())
                            .build();

                    com.aptos.request.v1.model.Resource bidTokenResource = com.aptos.request.v1.model.Resource.builder()
                            .moduleAddress(bidToken.getModuleAddress())
                            .moduleName(bidToken.getModuleName())
                            .resourceName(bidToken.getStructName())
                            .build();

                    TransactionPayload transactionPayload = TransactionPayload.builder()
                            .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                            .function(function)
                            .arguments(List.of())
                            .typeArguments(List.of(askTokenResource.resourceTag(), bidTokenResource.resourceTag()))
                            .build();

                    var response = AptosService.getAptosClient().requestSubmitTransaction(
                            ContextService.getKikoOwner(),
                            transactionPayload);
                    if (response.isValid()) {
                        return false;
                    }

                    if (!AptosService.checkTransaction(response.getData().getHash())) {
                        return false;
                    }

                    pair = Pair.builder()
                            .contract(function)
                            .askToken(askToken.getId())
                            .bidToken(bidToken.getId())
                            .build();

                    pairService.save(pair);
                }

            }
        }

        return true;
    }

    @Override
    public BoxGroupDto.BoxGroupInfo getBoxGroupById(Context context, String boxId) {
        QueryWrapper<BoxGroup> boxGroupQueryWrapper = new QueryWrapper<>();
        boxGroupQueryWrapper.lambda().eq(BoxGroup::getId, boxId);
        var boxGroup = this.getOne(boxGroupQueryWrapper);

        Map tokenMap = tokenService.cacheMap(context);
        Token askToken = (Token) tokenMap.get(String.valueOf(boxGroup.getAskToken()));
        Token bitToken = (Token) tokenMap.get(String.valueOf(boxGroup.getBidToken()));
        BoxGroupDto.BoxGroupInfo boxGroupInfo = BoxGroupDto.BoxGroupInfo.builder()
                .id(boxGroup.getId())
                .chain(boxGroup.getChain())
                .displayName(boxGroup.getDisplayName())
                .nftGroup(boxGroup.getNftGroup())
                .askToken(askToken)
                .bidToken(bitToken)
                .price(boxGroup.getPrice())
                .description(boxGroup.getDescription())
                .rule(boxGroup.getRule())
                .ts(boxGroup.getTs())
                .sort(boxGroup.getSort())
                .url(boxGroup.getUri())
                .build();
        return boxGroupInfo;
    }

    @Override
    public List<MyBoxDto> getMyBox(Context context, String account, String saleState) {
        QueryWrapper<BoxGroup> boxGroupQueryWrapper = new QueryWrapper<>();
        boxGroupQueryWrapper.lambda().eq(BoxGroup::getChain, context.getChain());
        boxGroupQueryWrapper.lambda().eq(BoxGroup::getIsEnabled, Boolean.TRUE);
        boxGroupQueryWrapper.lambda().eq(BoxGroup::getTransactionStatus, TransactionStatus.STATUS_3_SUCCESS.getCode());
        boxGroupQueryWrapper.lambda().orderByDesc(BoxGroup::getId);
        var boxGroups = super.list(boxGroupQueryWrapper);

        List<MyBoxDto> resultList = new ArrayList<>();
        if ("unSale".equals(saleState)) {
            resultList = getMyBoxUnSale(context, account, boxGroups);
        }else if ("onSale".equals(saleState)) {
            resultList = getMyboxOnSale(context, account, boxGroups);
        }
        return resultList;
    }


    public List<MyBoxDto> getMyBoxUnSale(Context context, String account, List<BoxGroup> boxGroups) {

        List<MyBoxDto> boxs = new ArrayList<>();
        boxGroups.forEach(b -> {
            Token token = tokenService.getById(b.getAskToken());
            //从链上查询余额
            com.aptos.request.v1.model.Resource resource = com.aptos.request.v1.model.Resource.builder().
                    moduleAddress(token.getModuleAddress())
                    .moduleName(token.getModuleName())
                    .resourceName(token.getStructName())
                    .build();

            Response<CoinStore> coinStoreResponse = AptosService.getAptosClient().requestCoinStore(account, resource);
            CoinStore coinStore = coinStoreResponse.getData();
            if (!Objects.isNull(coinStore)) {
                Long num = Long.valueOf(coinStore.getData().getCoin().getValue());
                for (int i = 0; i < num; i++) {
                    MyBoxDto myBoxDto = MyBoxDto.builder()
                            .id(token.getId())
                            .boxGroupId(b.getId())
                            .chain(token.getChain())
                            .coinId(token.getModuleAddress() + "::" + token.getModuleName() + "::" + token.getStructName())
                            .name(token.getName())
                            .symbol(token.getSymbol())
                            .decimals(token.getDecimals())
                            .uri(b.getUri())
                            .build();

                    boxs.add(myBoxDto);
                }
            }
        });




        return boxs;
    }

    public List<MyBoxDto> getMyboxOnSale(Context context, String account, List<BoxGroup> boxGroups) {
        var marketList = marketService.getMarketListByAccount(context, account, MarketTokenType.BOX.getType());
        if (marketList.size() == 0) {
            return null;
        }
        var marketMap = marketList.stream().collect(Collectors.toMap(market -> market.getOrderId(), (market) -> market, (key1, key2) -> key2));
        var boxGroupMap = boxGroups.stream().collect(Collectors.toMap(boxGroup -> boxGroup.getAskToken(), (boxGroup )-> boxGroup));
        List<MyBoxDto> resultList = new ArrayList<>();
        marketMap.forEach((key, value) ->{
            String[] askTokenInfo = value.getAskToken().split("::");
            Token token = tokenService.getByTokenInfo(context, askTokenInfo[0], askTokenInfo[1], askTokenInfo[2]);
            BoxGroup boxGroup = boxGroupMap.get(token.getId());
            MyBoxDto myBoxDto = MyBoxDto.builder()
                    .id(token.getId())
                    .orderId(value.getOrderId())
                    .boxGroupId(boxGroup.getId())
                    .chain(token.getChain())
                    .coinId(token.getModuleAddress() + "::" + token.getModuleName() + "::" + token.getStructName())
                    .name(token.getName())
                    .symbol(token.getSymbol())
                    .decimals(token.getDecimals())
                    .uri(boxGroup.getUri())
                    .orderId(value.getOrderId())
                    .saleType(value.getType())
                    .price(value.getPrice())
                    .bidder(value.getBidder())
                    .bidPrice(value.getBidAmount())
                    .auctionPrice(value.getBidAmount())
                    .ts(value.getTs())
                    .build();

            if (StringUtils.isNotEmpty(value.getBidToken())) {
                String[] tokenInfo = value.getBidToken().split("::");
                myBoxDto.setBidToken(tokenService.getByTokenInfo(context, tokenInfo[0], tokenInfo[1], tokenInfo[2]));
            }
            resultList.add(myBoxDto);
        });
        return resultList;
    }

    @Override
    public MyBoxDto getBoxById(Context context, String boxGroupId, String account, String saleState, String orderId) {
        BoxGroup boxGroup = this.getById(boxGroupId);
        changeLanguage(context, boxGroup);
        if (Objects.isNull(boxGroup)) {
            return null;
        }

        Token token = tokenService.getById(boxGroup.getAskToken());
        MyBoxDto myBoxDto = MyBoxDto.builder()
                .id(token.getId())
                .boxGroupId(boxGroup.getId())
                .chain(token.getChain())
                .coinId(token.getModuleAddress() + "::" + token.getModuleName() + "::" + token.getStructName())
                .name(token.getName())
                .symbol(token.getSymbol())
                .decimals(token.getDecimals())
                .uri(boxGroup.getUri())
                .creator(token.getModuleAddress())
                .owner(account)
                .desc(boxGroup.getDescription())
                .build();

        if ("onSale".equals(saleState)) {
            var marketList = marketService.getMarketListByOrderId(context, orderId);
            if (marketList.size() < 1) {
                return myBoxDto;
            }
            Market market = marketList.get(marketList.size() - 1);
            myBoxDto.setOrderId(market.getOrderId());
            myBoxDto.setSaleType(market.getType());
            myBoxDto.setPrice(market.getPrice());
            myBoxDto.setBidder(market.getBidder());
            myBoxDto.setBidPrice(market.getBidAmount());
            myBoxDto.setTs(market.getTs());
            myBoxDto.setOwner(market.getMaker());
            myBoxDto.setAuctionPrice(market.getBidAmount());
            if (StringUtils.isNotEmpty(market.getBidToken())) {
                String[] tokenInfo = market.getBidToken().split("::");
                myBoxDto.setBidToken(tokenService.getByTokenInfo(context, tokenInfo[0], tokenInfo[1], tokenInfo[2]));
            }

        }
        return myBoxDto;
    }

    @Override
    public List<Token> getListByTokenIds(Context context, List<String> tokenIdList) {
        QueryWrapper<BoxGroup> boxGroupQueryWrapper = new QueryWrapper<>();
        boxGroupQueryWrapper.lambda().eq(BoxGroup::getChain, context.getChain());
        boxGroupQueryWrapper.lambda().eq(BoxGroup::getIsEnabled, Boolean.TRUE);
        boxGroupQueryWrapper.lambda().in(BoxGroup::getAskToken, tokenIdList);
        //TODO：补充完整
        return null;
    }

    @Override
    public BoxGroup getByTokenId(Context context, String tokenId) {
        Token token = tokenService.getByTokenInfo(context, tokenId.split("::")[0], tokenId.split("::")[1], tokenId.split("::")[2]);
        QueryWrapper<BoxGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(BoxGroup::getAskToken, token.getId());
        BoxGroup boxGroup = this.getOne(queryWrapper);

        changeLanguage(context, boxGroup);
        return boxGroup;
    }

    @Override
    public BoxGroupDto getSaleList(Context context, Integer pageStart, Integer pageLimt) {
        QueryWrapper<BoxGroup> boxGroupQueryWrapper = new QueryWrapper<>();
        boxGroupQueryWrapper.lambda().eq(BoxGroup::getChain, context.getChain());
        boxGroupQueryWrapper.lambda().eq(BoxGroup::getIsEnabled, Boolean.TRUE);
        boxGroupQueryWrapper.lambda().eq(BoxGroup::getTransactionStatus, TransactionStatus.STATUS_3_SUCCESS);
        if (Objects.isNull(pageStart) || Objects.isNull(pageLimt)) {
            pageStart = 1;
            pageLimt = Integer.MAX_VALUE;
        }
        Page<BoxGroup> page = Page.of(pageStart, pageLimt);
        var pageResult = this.page(page, boxGroupQueryWrapper);
        var boxGroups = pageResult.getRecords();
        changeLanguage(context, boxGroups);

        Map tokenMap = tokenService.cacheMap(context);

        List<BoxGroupDto.BoxGroupInfo> resultList = new ArrayList<>();
        boxGroups.forEach(boxGroup -> {
            if (!boxGroup.getTransactionStatus().equals(TransactionStatus.STATUS_3_SUCCESS.getCode())) {
                return;
            }
            Object askToken =  tokenMap.get(String.valueOf(boxGroup.getAskToken()));
            Object bitToken =  tokenMap.get(String.valueOf(boxGroup.getBidToken()));
            BoxGroupDto.BoxGroupInfo boxGroupInfo = BoxGroupDto.BoxGroupInfo.builder()
                    .id(boxGroup.getId())
                    .chain(boxGroup.getChain())
                    .displayName(boxGroup.getDisplayName())
                    .nftGroup(boxGroup.getNftGroup())
                    .askToken(askToken)
                    .bidToken(bitToken)
                    .price(boxGroup.getPrice())
                    .description(boxGroup.getDescription())
                    .rule(boxGroup.getRule())
                    .ts(boxGroup.getTs())
                    .sort(boxGroup.getSort())
                    .url(boxGroup.getUri())
                    .build();
            resultList.add(boxGroupInfo);

        });

        BoxGroupDto boxGroupDto = BoxGroupDto.builder()
                .pages(pageResult.getPages())
                .total(pageResult.getTotal())
                .currentPage(pageResult.getCurrent())
                .boxGroupList(resultList)
                .build();

        //TODO:根据前端设计是否需要其他过滤条件

        return boxGroupDto;
    }

    private void changeLanguage(Context context, List<BoxGroup> list) {
        Set setDisplayName = list.stream().map(e -> e.getDisplayName()).collect(Collectors.toSet());
        Set setDescription = list.stream().map(e -> e.getDescription()).collect(Collectors.toSet());
        Set setRule = list.stream().map(e -> e.getRule()).collect(Collectors.toSet());

        Map mapDisplayName = languageService.getByKeys(context, setDisplayName);
        Map mapDescription = languageService.getByKeys(context, setDescription);
        Map mapRule = languageService.getByKeys(context, setRule);

        list.forEach(e -> {
            e.setDisplayName(mapDisplayName.get(e.getDisplayName()).toString());
            e.setDescription(mapDescription.get(e.getDescription()).toString());
            e.setRule(mapRule.get(e.getRule()).toString());
        });
    }


    private void changeLanguage(Context context, BoxGroup boxGroup) {
        boxGroup.setDisplayName(languageService.getByKey(context, boxGroup.getDisplayName()));
        boxGroup.setRule(languageService.getByKey(context, boxGroup.getRule()));
        boxGroup.setDescription(languageService.getByKey(context, boxGroup.getDescription()));
    }

}