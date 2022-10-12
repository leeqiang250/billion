package com.billion.service.aptos.kiko;

import com.aptos.AptosClient;
import com.aptos.request.v1.model.CoinInfo;
import com.aptos.request.v1.model.Resource;
import com.aptos.request.v1.model.Response;
import com.aptos.request.v1.model.TransactionPayload;
import com.aptos.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.TokenMapper;
import com.billion.model.dto.Context;
import com.billion.model.entity.Token;
import com.billion.model.enums.Chain;
import com.billion.model.enums.TransactionStatus;
import com.billion.model.exception.BizException;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
import com.billion.service.aptos.ContextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
    TokenMapper tokenMapper;

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
        for (int i = 0; i < tokens.size(); i++) {
            var token = tokens.get(i);
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
                    .function(ContextService.getTokenOwner() + token.getInitializeFunction())
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

            //TODO 删除
            {
                TransactionPayload transactionPayload2 = TransactionPayload.builder()
                        .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                        .function(ContextService.getTokenOwner() + "::coin_help::mint")
                        .arguments(List.of(
                                ContextService.getTokenOwner(),
                                "9999999999"
                        ))
                        .typeArguments(List.of(resource.resourceTag()))
                        .build();

                var response2 = AptosService.getAptosClient().requestSubmitTransaction(
                        ContextService.getTokenOwner(),
                        transactionPayload2);

                AptosService.checkTransaction(response2.getData().getHash());
            }
            //TODO 删除

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

    public Collection getListByScene(Context context, String scene) {
        List<Token> tokenList = tokenMapper.selectByScene(context.getChain(), scene);
        tokenList.forEach(t -> {
            if (StringUtils.isEmpty(t.getSymbol()) || StringUtils.isEmpty(t.getName())) {
                CoinInfo coinInfo = getCoinInfoFromChain(t);

                t.setDecimals(coinInfo.getData().getDecimals());
                t.setName(coinInfo.getData().getName());
                t.setSymbol(coinInfo.getData().getSymbol());
                this.updateById(t);
            }
        });
        return tokenList;
    }

    private CoinInfo getCoinInfoFromChain(Token token) {
        Resource resource = Resource.builder().
                moduleAddress(token.getModuleAddress())
                .moduleName(token.getModuleName())
                .resourceName(token.getStructName())
                .build();

        Response<CoinInfo> coinInfoResponse =  AptosService.getAptosClient().requestCoinInfo(token.getModuleAddress(), resource);
        CoinInfo coinInfo = coinInfoResponse.getData();
        return coinInfo;
    }

}