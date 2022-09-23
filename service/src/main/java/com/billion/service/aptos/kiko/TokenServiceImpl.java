package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Resource;
import com.aptos.request.v1.model.TransactionPayload;
import com.aptos.utils.StringUtils;
import com.billion.dao.aptos.kiko.TokenMapper;
import com.billion.model.constant.RequestPath;
import com.billion.model.entity.Token;
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
        var token = super.getById(id);
        if (Objects.isNull(token)) {
            return false;
        }

        if (StringUtils.isEmpty(token.getInitializeHash())) {
            this.checkToken(token);

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

            var transaction = AptosService.getAptosClient().requestSubmitTransaction(
                    token.getModuleAddress(),
                    transactionPayload);

            token.setInitializeHash(transaction.getHash());
        }

        if (!AptosService.checkTransaction(token.getInitializeHash())) {
            token.setInitializeHash(RequestPath.EMPTY);
        }

        super.updateById(token);

        //TODO 删除缓存

        return StringUtils.isEmpty(token.getInitializeHash());
    }

    public boolean transferResource(String from, String to, String amount, Resource resource) {
        var transaction = AptosService.getAptosClient().transferResource(from, to, amount, resource);

        return AptosService.checkTransaction(transaction.getHash());
    }

}