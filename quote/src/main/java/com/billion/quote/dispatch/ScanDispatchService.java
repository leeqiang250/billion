package com.billion.quote.dispatch;

import com.alibaba.fastjson2.JSONObject;
import com.aptos.request.v1.model.Transaction;
import com.aptos.utils.StringUtils;
import com.billion.model.entity.Config;
import com.billion.model.event.*;
import com.billion.model.exception.BizException;
import com.billion.service.aptos.AptosService;
import com.billion.service.aptos.ContextService;
import com.billion.service.aptos.kiko.*;
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
public class ScanDispatchService implements Serializable {

    @Resource
    ConfigService configService;

    @Resource
    MarketService marketService;

    @Resource
    NftService nftService;

    @Resource
    DistributedLockService distributedLockService;

    boolean next;

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

    //@Scheduled(cron = "*/2 * * * * ?")
    void dispatch() {
        this.next = true;
        while (this.next) {
            this.distributedLockService.tryGetDistributedLock(
                    SCAN_CHAIN_LOCK,
                    UUID.randomUUID().toString(),
                    LOCK_EXPIRE_TS,
                    (Consumer<Object>) o -> {
                        this.scan();
                    }
            );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    void scan() {
        long version = Long.parseLong(Config.SCAN_CHAIN_CURSOR.getValue());
        version++;

        var response = AptosService.getAptosClient().requestTransaction(String.valueOf(version));
        if (response.isValid()
                || Objects.isNull(response.getData())
                || response.getData().isEmpty()) {
            this.next = false;
            return;
        }

        log.info("------------------------------------------------------------------------------------------------");
        log.info("transaction size {}", response.getData().size());
        log.info("start {} stop {}", response.getData().get(0).getVersion(), response.getData().get(response.getData().size() - 1).getVersion());

        boolean valid = false;
        var transactions = response.getData();
        for (int i = 0; i < transactions.size(); i++) {
            var transaction = transactions.get(i);
            if (!Objects.isNull(transaction.getEvents())) {
                valid = false;

                var events = transaction.getEvents();
                for (int j = 0; j < events.size(); j++) {
                    var event = events.get(j);

                    if (nftService.isNftCreateTokenDataEvent(event)
                            || nftService.isOpenBoxEvent(event)
                            || nftService.isNftDepositEvent(event)
                            || nftService.isNftDepositEvent(event)
                    ) {
                        log.info(event.getType());

                        valid = true;
                        break;
                    }

                    if (ContextService.getKikoOwner().contains(event.getType().split("::")[0])) {
                        if (marketService.isBoxMakerEvent(event)
                                || marketService.isBoxTakerEvent(event)
                                || marketService.isBoxBidEvent(event)
                                || marketService.isBoxCancelEvent(event)
                                || marketService.isNftMakerEvent(event)
                                || marketService.isNftTakerEvent(event)
                                || marketService.isNftBidEvent(event)
                                || marketService.isNftCancelEvent(event)
                        ) {
                            log.info(event.getType());

                            valid = true;
                            break;
                        }
                    }
                }

                if (valid) {
                    Long transactionVersion = Long.parseLong(transaction.getVersion());
                    nftService.removeGe(transactionVersion);
                    marketService.removeGe(transactionVersion);

                    this.process(transaction);
                }
            }

            Config.SCAN_CHAIN_CURSOR.setValue(transaction.getVersion());
        }

        this.configService.updateById(Config.SCAN_CHAIN_CURSOR);

        log.info("{}", Config.SCAN_CHAIN_CURSOR);
        log.info("------------------------------------------------------------------------------------------------");

        this.next = true;
    }

    void process(Transaction transaction) {
        var events = transaction.getEvents();
        for (int j = 0; j < events.size(); j++) {
            var event = events.get(j);

            if (nftService.isNftCreateTokenDataEvent(event)
                    && ContextService.getKikoOwner().equals(event.getGuid().getAccountAddress())) {
                NftCreateTokenDataEvent nftCreateTokenDataEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), NftCreateTokenDataEvent.class);
                nftService.addNftCreateTokenDataEvent(transaction, event, nftCreateTokenDataEvent);
            } else if (nftService.isNftWithdrawEvent(event)) {
                NftWithdrawEvent nftWithdrawEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), NftWithdrawEvent.class);
                if (ContextService.getKikoOwner().equals(nftWithdrawEvent.getId().getTokenDataId().getCreator())) {
                    nftService.addNftWithdrawEvent(transaction, event, nftWithdrawEvent);
                }
            } else if (nftService.isNftDepositEvent(event)) {
                NftDepositEvent nftDepositEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), NftDepositEvent.class);
                if (ContextService.getKikoOwner().equals(nftDepositEvent.getId().getTokenDataId().getCreator())) {
                    nftService.addNftDepositEvent(transaction, event, nftDepositEvent);
                }
            } else if (nftService.isOpenBoxEvent(event)) {
                OpenBoxEvent openBoxEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), OpenBoxEvent.class);
                log.info(event.getType());
                log.info(openBoxEvent.toString());
            }
            if (ContextService.getKikoOwner().contains(event.getType().split("::")[0])) {
                if (marketService.isBoxMakerEvent(event)) {
                    BoxMakerEvent boxMakerEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), BoxMakerEvent.class);
                    marketService.addBoxMakerEvent(transaction, event, boxMakerEvent);
                } else if (marketService.isBoxTakerEvent(event)) {
                    BoxTakerEvent boxTakerEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), BoxTakerEvent.class);
                    marketService.addBoxTakerEvent(transaction, event, boxTakerEvent);
                } else if (marketService.isBoxBidEvent(event)) {
                    BoxBidEvent boxBidEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), BoxBidEvent.class);
                    marketService.addBoxBidEvent(transaction, event, boxBidEvent);
                } else if (marketService.isBoxCancelEvent(event)) {
                    BoxCancelEvent boxCancelEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), BoxCancelEvent.class);
                    marketService.addBoxCancelEvent(transaction, event, boxCancelEvent);
                } else if (marketService.isNftMakerEvent(event)) {
                    NftMakerEvent nftMakerEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), NftMakerEvent.class);
                    marketService.addNftMakerEvent(transaction, event, nftMakerEvent);
                } else if (marketService.isNftTakerEvent(event)) {
                    NftTakerEvent nftTakerEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), NftTakerEvent.class);
                    marketService.addNftTakerEvent(transaction, event, nftTakerEvent);
                } else if (marketService.isNftBidEvent(event)) {
                    NftBidEvent nftBidEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), NftBidEvent.class);
                    marketService.addNftBidEvent(transaction, event, nftBidEvent);
                } else if (marketService.isNftCancelEvent(event)) {
                    NftCancelEvent nftCancelEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), NftCancelEvent.class);
                    marketService.addNftCancelEvent(transaction, event, nftCancelEvent);
                }
            }
        }
    }

}