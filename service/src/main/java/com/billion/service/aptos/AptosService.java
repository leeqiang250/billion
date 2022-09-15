package com.billion.service.aptos;

import com.aptos.request.v1.response.ResponseTransaction;
import com.aptos.utils.AptosClient;
import com.billion.framework.util.Retrying;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class AptosService {

    @Value("${aptos.host}")
    private String aptosHost;

    @Getter
    static AptosClient aptosClient;

    @PostConstruct
    public void init() {
        AptosService.aptosClient = new AptosClient(this.aptosHost);
        log.info("Aptos{}", AptosService.aptosClient.requestNode());
    }

    public boolean checkTransaction(String hash) {
        log.info("合约hash:{}", hash);
        return Retrying.retry(
                () -> {
                    ResponseTransaction responseTransaction = AptosService.aptosClient.requestTransactionByHash(hash);
                    if (responseTransaction == null) {
                        throw new RuntimeException("合约执行中... " + "txn:" + hash);
                    } else {
                        if (responseTransaction.isSuccess()) {
                            log.info("合约执行成功，result: {}", responseTransaction);
                            return true;
                        } else {
                            log.info("合约执行失败，result:{}", responseTransaction);
                            return false;
                        }
                    }
                },
                60,
                2000,
                Exception.class
        );
    }

}