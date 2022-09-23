package com.billion.service.aptos;

import com.aptos.AptosClient;
import com.aptos.request.v1.model.Node;
import com.aptos.request.v1.model.Transaction;
import com.billion.framework.util.RetryingUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class AptosService {

    final long cacheTs = 5000L;

    @Getter
    static AptosClient aptosClient;

    @Resource
    ContextService contextService;

    @PostConstruct
    public void init() {
        AptosService.aptosClient = new AptosClient(ContextService.getAptosHost());
        log.info("Aptos{}", AptosService.aptosClient.requestNode());
    }

    public boolean checkTransaction(String hash) {
        return RetryingUtils.retry(
                () -> {
                    Transaction responseTransaction = null;
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

    Node responseNode;

    long responseNodeTs;

    public Node requestNodeCache() {
        if (Objects.isNull(this.responseNode) || System.currentTimeMillis() > (this.responseNodeTs + this.cacheTs)) {
            Node responseNode = AptosService.getAptosClient().requestNode();
            if (Objects.nonNull(responseNode)) {
                this.responseNodeTs = System.currentTimeMillis();
                this.responseNode = responseNode;
            }
        }

        return this.responseNode;
    }

}