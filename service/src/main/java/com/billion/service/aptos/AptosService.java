package com.billion.service.aptos;

import com.aptos.request.v1.response.ResponseTransaction;
import com.aptos.utils.AptosClient;
import com.billion.framework.util.Retrying;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Objects;

@Slf4j
@Service
public class AptosService {

    @Getter
    static AptosClient aptosClient;

    @Resource
    ContextService contextService;

    @PostConstruct
    public void init() {
        AptosService.aptosClient = new AptosClient(this.contextService.aptosHost);
        log.info("Aptos{}", AptosService.aptosClient.requestNode());
    }

    public boolean checkTransaction(String hash) {
        return Retrying.retry(
                () -> {
                    ResponseTransaction responseTransaction = null;
                    try {
                        responseTransaction = AptosService.aptosClient.requestTransactionByHash(hash);
                    } catch (Exception exception) {
                    }
                    if (Objects.isNull(responseTransaction)) {
                        throw new RuntimeException("transaction non-existent:" + hash);
                    } else {
                        log.info("result:{} transaction:{}", responseTransaction.isSuccess(), responseTransaction.getHash());
                        return responseTransaction.isSuccess();
                    }
                },
                60,
                1000L,
                Exception.class
        );
    }

}