package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Transaction;
import com.aptos.request.v1.model.TransactionPayload;
import com.aptos.utils.Hex;
import com.billion.dao.aptos.kiko.TokenMapper;
import com.billion.model.entity.Token;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class TokenServiceImpl extends AbstractCacheService<TokenMapper, Token> implements TokenService {


    @Override
    public Transaction initialize(Token token) {
        com.aptos.request.v1.model.Resource resource = com.aptos.request.v1.model.Resource.builder()
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


        return AptosService.getAptosClient().requestSubmitTransaction(
                token.getModuleAddress(),
                transactionPayload);
    }



//    @Override
//    public Transaction transferApt(String from, String to, String amount) {
//        return AptosService.getAptosClient().transferApt(from, to, amount);
//    }

}