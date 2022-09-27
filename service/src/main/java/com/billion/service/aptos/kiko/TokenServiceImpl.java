package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Resource;
import com.aptos.request.v1.model.TransactionPayload;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.TokenMapper;
import com.billion.model.entity.Token;
import com.billion.model.enums.TransactionStatus;
import com.billion.model.exception.BizException;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class TokenServiceImpl extends AbstractCacheService<TokenMapper, Token> implements TokenService {

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
    public boolean initialize(Serializable id) {
        QueryWrapper<Token> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Token::getId, id);
        wrapper.lambda().eq(Token::getTransactionStatus, TransactionStatus.STATUS_1_READY.getCode());
        var token = super.getOne(wrapper, false);
        if (Objects.isNull(token)) {
            return false;
        }

        if (!this.checkToken(token)) {
            return false;
        }

        Resource resource = Resource.builder()
                .moduleAddress(token.getModuleAddress())
                .moduleName(token.getModuleName())
                .resourceName(token.getStructName())
                .build();

        TransactionPayload transactionPayload = TransactionPayload.builder()
                .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                .function(token.getInitializeFunction())
                .arguments(List.of(
                        token.getName(),
                        token.getSymbol()
                ))
                .typeArguments(List.of(resource.resourceTag()))
                .build();

        var response = AptosService.getAptosClient().requestSubmitTransaction(
                token.getModuleAddress(),
                transactionPayload);
        if (response.isValid()) {
            return false;
        }

        if (!AptosService.checkTransaction(response.getData().getHash())) {
            token.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            return false;
        }

        token.setTransactionHash(response.getData().getHash());
        token.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);

        super.updateById(token);

        //TODO 删除缓存

        return true;
    }

    public boolean transferResource(String from, String to, String amount, Resource resource) {
        var transaction = AptosService.getAptosClient().transferResource(from, to, amount, resource);

        return AptosService.checkTransaction(transaction.getData().getHash());
    }

}