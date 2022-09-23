package com.billion.service.aptos;

import com.aptos.AptosClient;
import com.aptos.request.v1.model.Node;
import com.aptos.request.v1.model.Transaction;
import com.aptos.utils.StringUtils;
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

    @Getter
    static AptosClient aptosClient;

    @Resource
    ContextService contextService;

    @PostConstruct
    public void init() {
        AptosService.aptosClient = new AptosClient(ContextService.getAptosHost());
        log.info("Aptos{}", AptosService.aptosClient.requestNode());
    }

    public static boolean checkTransaction(String hash) {
        if (StringUtils.isEmpty(hash)) {
            return false;
        }

        return RetryingUtils.retry(
                () -> {
                    Transaction transaction = null;
                    try {
                        transaction = AptosService.aptosClient.requestTransactionByHash(hash);
                    } catch (Exception exception) {
                    }
                    if (Objects.isNull(transaction)) {
                        throw new RuntimeException("transaction non-existent:" + hash);
                    } else {
                        log.info("result:{} transaction:{}, vmStatus:{}", transaction.isSuccess(), transaction.getHash(), transaction.getVmStatus());
                        return transaction.isSuccess();
                    }
                },
                60,
                1000L,
                Exception.class
        );
    }

    final static long cacheTs = 5000L;

    static Node node;

    static long responseNodeTs;

    public static Node requestNodeCache() {
        if (Objects.isNull(node) || System.currentTimeMillis() > (responseNodeTs + cacheTs)) {
            node = AptosService.getAptosClient().requestNode();
            if (Objects.nonNull(node)) {
                responseNodeTs = System.currentTimeMillis();
            }
        }

        return node;
    }

}