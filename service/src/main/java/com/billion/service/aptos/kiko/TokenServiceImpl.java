package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.CoinInfo;
import com.aptos.request.v1.model.Resource;
import com.aptos.request.v1.model.Response;
import com.aptos.request.v1.model.TransactionPayload;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.TokenMapper;
import com.billion.dao.aptos.kiko.TokenSceneMapper;
import com.billion.model.dto.Context;
import com.billion.model.entity.Token;
import com.billion.model.entity.TokenScene;
import com.billion.model.enums.CacheTsType;
import com.billion.model.enums.Chain;
import com.billion.model.enums.TransactionStatus;
import com.billion.model.exception.BizException;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
import com.billion.service.aptos.ContextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.billion.model.constant.RequestPath.EMPTY;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class TokenServiceImpl extends AbstractCacheService<TokenMapper, Token> implements TokenService {

    @javax.annotation.Resource
    TokenSceneMapper tokenSceneMapper;

    @Override
    public Map cacheMap(Context context) {
        String key = this.cacheMapKey("ids::" + context.getChain());

        Map map = this.getRedisTemplate().opsForHash().entries(key);
        if (!map.isEmpty()) {
            return map;
        }

        QueryWrapper<Token> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Token::getChain, context.getChain());
        List<Token> list = super.list(wrapper);

        map = list.stream().collect(Collectors.toMap(e -> e.getId().toString(), (e) -> e));
        this.getRedisTemplate().opsForHash().putAll(key, map);
        this.getRedisTemplate().expire(key, this.cacheSecond(CacheTsType.MIDDLE));

        return map;
    }

    public boolean checkToken(Token token) {
        //TODO 正则校验
        //if (token.getName().matches(NUMBER_AND_CHARACTER)) {
        //    throw new RuntimeException("name format error");
        //}

        //TODO 正则校验
        //if (token.getSymbol().matches(NUMBER_AND_CHARACTER)) {
        //    throw new RuntimeException("symbol format error");
        //}

        if (32 < token.getName().length()) {
            throw new BizException("name too long, max 32");
        }

        if (10 < token.getSymbol().length()) {
            throw new BizException("symbol too long, max 10");
        }

        return true;
    }

    @Override
    public boolean initialize() {
        QueryWrapper<Token> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Token::getChain, Chain.APTOS.getCode());
        wrapper.lambda().eq(Token::getTransactionStatus, TransactionStatus.STATUS_1_READY.getCode());
        var tokens = super.list(wrapper);
        for (Token token : tokens) {
            if (!this.checkToken(token)) {
                token.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
                super.updateById(token);

                return false;
            }

            Resource resource = Resource.builder()
                    .moduleAddress(token.getModuleAddress())
                    .moduleName(token.getModuleName())
                    .resourceName(token.getStructName())
                    .build();

            TransactionPayload transactionPayload = TransactionPayload.builder()
                    .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                    .function(ContextService.getKikoOwner() + "::help::initialize_coin")
                    .arguments(List.of(
                            token.getName(),
                            token.getSymbol(),
                            token.getDecimals()
                    ))
                    .typeArguments(List.of(resource.resourceTag()))
                    .build();

            var response = AptosService.getAptosClient().requestSubmitTransaction(
                    token.getModuleAddress(),
                    transactionPayload);
            if (response.isValid()) {
                token.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
                super.updateById(token);

                return false;
            }

            if (!AptosService.checkTransaction(response.getData().getHash())) {
                token.setTransactionHash(Objects.isNull(response.getData().getHash()) ? EMPTY : response.getData().getHash());
                token.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
                super.updateById(token);

                return false;
            }

            token.setTransactionHash(response.getData().getHash());
            token.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);

            var coinStore = AptosService.getAptosClient().requestCoinStore(ContextService.getKikoOwner(), resource);
            if (coinStore.isValid()
                    || 9999999999L > Long.parseLong(coinStore.getData().getData().getCoin().getValue())
            ) {
                //TODO 删除
                {
                    TransactionPayload transactionPayload2 = TransactionPayload.builder()
                            .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                            .function(ContextService.getKikoOwner() + "::help::mint_coin")
                            .arguments(List.of(
                                    ContextService.getKikoOwner(),
                                    "9999999999"
                            ))
                            .typeArguments(List.of(resource.resourceTag()))
                            .build();

                    var response2 = AptosService.getAptosClient().requestSubmitTransaction(
                            ContextService.getKikoOwner(),
                            transactionPayload2);

                    AptosService.checkTransaction(response2.getData().getHash());
                }
                //TODO 删除
            }

            super.updateById(token);
        }

        //TODO 删除缓存

        return true;
    }

    public boolean transferResource(String from, String to, String amount, Resource resource) {
        var transaction = AptosService.getAptosClient().transferResource(from, to, amount, resource);

        return AptosService.checkTransaction(transaction.getData().getHash());
    }

    @Override
    public List<Token> getByScene(Context context, String scene) {
        QueryWrapper<TokenScene> tokenSceneQueryWrapper = new QueryWrapper<>();
        tokenSceneQueryWrapper.lambda().eq(TokenScene::getScene, scene);
        var tokenScenes = this.tokenSceneMapper.selectList(tokenSceneQueryWrapper);

        var ids = tokenScenes.stream().map(TokenScene::getTokenId).collect(Collectors.toList());

        QueryWrapper<Token> tokenQueryWrapper = new QueryWrapper<>();
        tokenQueryWrapper.lambda().in(Token::getId, ids);
        tokenQueryWrapper.lambda().eq(Token::getTransactionStatus, TransactionStatus.STATUS_3_SUCCESS.getCode());

        //TODO 缓存

        return super.list(tokenQueryWrapper);
    }

    @Override
    public List<Token> getByCoinIdList(Context context, List<String> coinIdList) {
        if (Objects.isNull(coinIdList) || coinIdList.size() == 0) {
            return new ArrayList<>();
        }
        List<Token> resultList = new ArrayList<>();
        coinIdList.forEach(id -> {
            QueryWrapper<Token> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Token::getChain, context.getChain());
            queryWrapper.lambda().eq(Token::getModuleAddress, id.split("::")[0]);
            queryWrapper.lambda().eq(Token::getModuleName, id.split("::")[1]);
            queryWrapper.lambda().eq(Token::getStructName, id.split("::")[2]);
            resultList.add(this.getOne(queryWrapper));
        });
        return resultList;
    }

    @Override
    public Token getByTokenInfo(Context context, String address, String moduleName, String struct) {
        QueryWrapper<Token> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Token::getChain, context);
        queryWrapper.lambda().eq(Token::getModuleAddress, address);
        queryWrapper.lambda().eq(Token::getModuleName, moduleName);
        queryWrapper.lambda().eq(Token::getStructName, struct);
        return this.getOneThrowEx(queryWrapper);
    }

    private CoinInfo getCoinInfoFromChain(Token token) {
        Resource resource = Resource.builder().
                moduleAddress(token.getModuleAddress())
                .moduleName(token.getModuleName())
                .resourceName(token.getStructName())
                .build();

        Response<CoinInfo> coinInfoResponse = AptosService.getAptosClient().requestCoinInfo(token.getModuleAddress(), resource);
        CoinInfo coinInfo = coinInfoResponse.getData();
        return coinInfo;
    }

}