package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.CoinInfo;
import com.aptos.request.v1.model.Response;
import com.aptos.request.v1.model.TransactionPayload;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.BoxGroupMapper;
import com.billion.model.dto.Context;
import com.billion.model.entity.BoxGroup;
import com.billion.model.entity.Pair;
import com.billion.model.entity.Token;
import com.billion.model.enums.*;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
import com.billion.service.aptos.ContextService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    LanguageService languageService;

    @Override
    public Map cacheMap(Context context) {
        String key = this.cacheMapKey("ids::" + context.getChain());

        Map map = this.getRedisTemplate().opsForHash().entries(key);
        if (!map.isEmpty()) {
            return map;
        }

        QueryWrapper<BoxGroup> wrapper = new QueryWrapper<>();
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

        for (int i = 0; i < boxGroups.size(); i++) {
            var askToken = tokenService.getById(boxGroups.get(i).getAskToken());

            for (int j = 0; j < tokens.size(); j++) {
                var bidToken = tokens.get(j);

                QueryWrapper<Pair> pairQueryWrapper = new QueryWrapper<>();
                pairQueryWrapper.lambda().eq(Pair::getContract, function);
                pairQueryWrapper.lambda().eq(Pair::getAskToken, askToken.getId());
                pairQueryWrapper.lambda().eq(Pair::getBidToken, bidToken.getId());
                var pair = pairService.getOne(pairQueryWrapper, false);
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
    public List<Token> getMyBox(Context context, String account) {
        QueryWrapper<BoxGroup> boxGroupQueryWrapper = new QueryWrapper<>();
        boxGroupQueryWrapper.lambda().eq(BoxGroup::getChain, context.getChain());
        boxGroupQueryWrapper.lambda().eq(BoxGroup::getIsEnabled, Boolean.TRUE);
        boxGroupQueryWrapper.lambda().eq(BoxGroup::getTransactionStatus, TransactionStatus.STATUS_3_SUCCESS.getCode());
        var boxGroups = super.list(boxGroupQueryWrapper);

        List<Token> boxs = new ArrayList<>();

        boxGroups.forEach(b -> {
            Token token = tokenService.getById(b.getAskToken());
            com.aptos.request.v1.model.Resource resource = com.aptos.request.v1.model.Resource.builder().
                    moduleAddress(token.getModuleAddress())
                    .moduleName(token.getModuleName())
                    .resourceName(token.getStructName())
                    .build();

            Response<CoinInfo> coinInfoResponse = AptosService.getAptosClient().requestCoinInfo(account, resource);
            if (!Objects.isNull(coinInfoResponse.getData())) {
                boxs.add(token);
            }
        });
        return boxs;
    }

    @Override
    public List<Token> getListByTokenIds(Context context) {
        return null;
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

}