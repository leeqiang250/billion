package com.billion.service.aptos;

import com.aptos.AptosClient;
import com.aptos.request.v1.model.Node;
import com.aptos.request.v1.model.Response;
import com.aptos.request.v1.model.Transaction;
import com.aptos.utils.StringUtils;
import com.billion.framework.util.Retrying;
import com.billion.model.exception.BizException;
import com.billion.service.aptos.kiko.LogChainService;
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
    LogChainService logChainService;

    public void initialize() {
        if (Objects.isNull(ContextService.getAptosHost())) {
            throw new BizException("aptos host is null");
        }
        AptosService.aptosClient = new AptosClient(ContextService.getAptosHost(), info -> this.logChainService.add(info), s -> log.info(s));
        log.info("Aptos Node[{}]", requestNodeCache());
    }

    public static boolean checkTransaction(String hash) {
        if (StringUtils.isEmpty(hash)) {
            return false;
        }

        return Retrying.retry(
                () -> {
                    var response = AptosService.aptosClient.requestTransactionByHash(hash);
                    if (response.isValid()) {
                        throw new RuntimeException("transaction non-existent:" + hash);
                    } else {
                        log.info("result[{}] transaction[{}] vmStatus[{}]", response.getData().isSuccess(), response.getData().getHash(), response.getData().getVmStatus());
                        return response.getData().isSuccess();
                    }
                },
                60,
                1000L,
                Exception.class
        );
    }

    public static Response<Transaction> getTransaction(String hash) {
        if (StringUtils.isEmpty(hash)) {
            return null;
        }

        return Retrying.retry(
                () -> {
                    var response = AptosService.aptosClient.requestTransactionByHash(hash);
                    if (response.isValid()) {
                        throw new RuntimeException("transaction non-existent:" + hash);
                    } else {
                        log.info("result[{}] transaction[{}] vmStatus[{}]", response.getData().isSuccess(), response.getData().getHash(), response.getData().getVmStatus());
                        return response;
                    }
                },
                60,
                1000L,
                Exception.class
        );
    }

    final static long cacheTs = 10000L;

    static Node node;

    static long responseNodeTs;

    public static Node requestNodeCache() {
        if (Objects.isNull(node) || System.currentTimeMillis() > (responseNodeTs + cacheTs)) {
            var response = AptosService.getAptosClient().requestNode();
            if (!response.isValid() && !Objects.isNull(response.getData())) {
                response.getData().setRpc(ContextService.getAptosHost());

                responseNodeTs = System.currentTimeMillis();
                node = response.getData();
            }
        }

        return node;
    }

}