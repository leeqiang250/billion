package com.billion.quote.sss;

import com.aptos.utils.StringUtils;
import com.billion.model.entity.Config;
import com.billion.model.exception.BizException;
import com.billion.service.aptos.AptosService;
import com.billion.service.aptos.kiko.ConfigService;
import com.billion.service.aptos.kiko.DistributedLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import static com.billion.model.constant.RedisPath.LOCK_EXPIRE_TS;
import static com.billion.model.constant.RedisPath.SCAN_CHAIN_LOCK;

/**
 * @author liqiang
 */
@Slf4j
@Component
@Configuration
@EnableScheduling
public class ScanService implements Serializable {

    @Resource
    ConfigService configService;

    @Resource
    DistributedLockService distributedLockService;

    @PostConstruct
    void init() {
        var config = this.configService.getById(Config.SCAN_CHAIN_CURSOR.getId());
        if (Objects.isNull(config) || StringUtils.isEmpty(config.getValue())) {
            this.configService.save(Config.SCAN_CHAIN_CURSOR);

            var node = AptosService.requestNodeCache();
            if (Objects.isNull(node)) {
                throw new BizException("node is null");
            }

            Config.SCAN_CHAIN_CURSOR.setValue(node.getLedgerVersion());
            if (!this.configService.saveOrUpdate(Config.SCAN_CHAIN_CURSOR)) {
                throw new BizException("update scan chain cursor");
            }
        } else {
            Config.SCAN_CHAIN_CURSOR = config;
        }
    }

    @Scheduled(cron = "*/1 * * * * ?")
    void dispatch() {
        this.distributedLockService.tryGetDistributedLock(
                SCAN_CHAIN_LOCK,
                UUID.randomUUID().toString(),
                LOCK_EXPIRE_TS,
                (Consumer<Object>) o -> {
                    this.scan();
                }
        );
    }

    @Transactional(rollbackFor = Exception.class)
    void scan() {
        long version = Long.parseLong(Config.SCAN_CHAIN_CURSOR.getValue());
        version++;

        var response = AptosService.getAptosClient().requestTransaction(String.valueOf(version));
        if (response.isValid() || response.getData().isEmpty()) {
            return;
        }

        log.info("------------------------------------------------------------------------------------------------");
        log.info("transaction size {}", response.getData().size());
        log.info("start {} stop {}", response.getData().get(0).getVersion(), response.getData().get(response.getData().size() - 1).getVersion());

        response.getData().forEach(transaction -> {
            Config.SCAN_CHAIN_CURSOR.setValue(transaction.getVersion());
        });

        this.configService.updateById(Config.SCAN_CHAIN_CURSOR);

        log.info("{}", Config.SCAN_CHAIN_CURSOR);
        log.info("------------------------------------------------------------------------------------------------");
    }

}