package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Resource;
import com.aptos.request.v1.model.TransactionPayload;
import com.aptos.utils.Hex;
import com.aptos.utils.StringUtils;
import com.billion.dao.aptos.kiko.TokenMapper;
import com.billion.model.constant.RequestPath;
import com.billion.model.entity.Token;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class TokenServiceImpl extends AbstractCacheService<TokenMapper, Token> implements TokenService {

    @Override
    public boolean initialize(Serializable id) {
        var token = super.getById(id);
        if (StringUtils.isEmpty(token.getInitializeHash())) {
            Resource resource = Resource.builder()
                    .moduleAddress(token.getModuleAddress())
                    .moduleName(token.getModuleName())
                    .resourceName(token.getStructName())
                    .build();

            TransactionPayload transactionPayload = TransactionPayload.builder()
                    .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                    .function(token.getInitializeFunction())
                    .arguments(List.of(
                            Hex.encode(token.getName()),
                            Hex.encode(token.getSymbol())
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