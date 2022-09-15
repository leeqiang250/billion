package com.billion.service.aptos;

import com.aptos.request.v1.response.ResponseTransaction;
import com.aptos.utils.AptosClient;
import com.billion.framework.util.Retrying;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Slf4j
@Service
public class AptosService {

    @Value("${aptos.host}")
    String aptosHost;

    @Getter
    static AptosClient aptosClient;

    @PostConstruct
    public void init() {
        AptosService.aptosClient = new AptosClient(this.aptosHost);
        log.info("Aptos{}", AptosService.aptosClient.requestNode());
    }

    int i;

    public boolean checkTransaction(String hash2) {
        return Retrying.retry(
                () -> {
                    i++;
                    String hash = hash2 + i;
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