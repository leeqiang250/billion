package com.billion.quote.dispatch;

import com.alibaba.fastjson2.JSONObject;
import com.aptos.request.v1.model.Event;
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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Map;
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
    NftComposeService nftComposeService;

    @Resource
    NftSplitService nftSplitService;

    @Resource
    NftService nftService;

    @Resource
    NftOpService nftOpService;

    @Resource
    DistributedLockService distributedLockService;

    @Resource
    OperationService operationService;

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

    //@Scheduled(fixedDelay = 3000)
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
        log.info("transaction size[{}]", response.getData().size());
        log.info("start[{}] stop[{}]", response.getData().get(0).getVersion(), response.getData().get(response.getData().size() - 1).getVersion());

        boolean valid = false;
        var transactions = response.getData();
        for (Transaction transaction : transactions) {
            log.info("scan node-version[{}] version[{}] hash[{}]", AptosService.requestNodeCache().getLedgerVersion(), transaction.getVersion(), transaction.getHash());

            if (Transaction.USER_TRANSACTION.equals(transaction.getType())
                    && !Objects.isNull(transaction.getEvents())) {
                valid = false;

                var events = transaction.getEvents();
                for (Event<Map> event : events) {
                    if (EventType.isOpenBoxEvent(event)
                            || EventType.isNftComposeEvent(event)
                            || EventType.isNftSplitEvent(event)
                    ) {
                        log.info(event.getType());

                        valid = true;
                        break;
                    }

                    if (this.isNftEvent(event)) {
                        log.info(event.getType());

                        valid = true;
                        break;
                    }

                    if (this.isMarketEvent(event)) {
                        log.info(event.getType());

                        valid = true;
                        break;
                    }
                }

                if (valid) {
                    Long transactionVersion = Long.parseLong(transaction.getVersion());
                    this.nftService.removeGe(transactionVersion);
                    this.marketService.removeGe(transactionVersion);
                    this.nftComposeService.removeGe(transactionVersion);
                    this.nftSplitService.removeGe(transactionVersion);

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
        for (Event<Map> event : events) {

            if (EventTypeNft.isNftCreateTokenDataEvent(event)
                    && ContextService.getKikoOwner().equals(event.getGuid().getAccountAddress())) {
                NftCreateTokenDataEvent nftCreateTokenDataEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), NftCreateTokenDataEvent.class);
                nftService.addNftCreateTokenDataEvent(transaction, event, nftCreateTokenDataEvent);
            } else if (EventTypeNft.isNftWithdrawEvent(event)) {
                NftWithdrawEvent nftWithdrawEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), NftWithdrawEvent.class);
                if (ContextService.getKikoOwner().equals(nftWithdrawEvent.getId().getTokenDataId().getCreator())) {
                    nftService.addNftWithdrawEvent(transaction, event, nftWithdrawEvent);
                }
            } else if (EventTypeNft.isNftDepositEvent(event)) {
                NftDepositEvent nftDepositEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), NftDepositEvent.class);
                if (ContextService.getKikoOwner().equals(nftDepositEvent.getId().getTokenDataId().getCreator())) {
                    nftService.addNftDepositEvent(transaction, event, nftDepositEvent);
                }
            } else if (EventTypeNft.isNftBurnTokenEvent(event)) {
                NftBurnTokenEvent nftBurnTokenEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), NftBurnTokenEvent.class);
                if (ContextService.getKikoOwner().equals(nftBurnTokenEvent.getId().getTokenDataId().getCreator())) {
                    nftService.addNftBurnTokenEvent(transaction, event, nftBurnTokenEvent);
                }
            }

            if (EventType.isOpenBoxEvent(event)) {
                OpenBoxEvent openBoxEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), OpenBoxEvent.class);
                operationService.addOpenBoxOpt(openBoxEvent, transaction);
            } else if (EventType.isNftSplitEvent(event)) {
                NftSplitEvent nftSplitEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), NftSplitEvent.class);
                nftOpService.addNftSplitEvent(transaction, nftSplitEvent);
            } else if (EventType.isNftComposeEvent(event)) {
                NftComposeEvent nftComposeEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), NftComposeEvent.class);
                nftOpService.addNftComposeEvent(transaction, nftComposeEvent);
            }

            if (EventTypeMarketBox.isMarketBoxMakerEvent(event)) {
                MarketBoxMakerEvent boxMakerEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), MarketBoxMakerEvent.class);
                marketService.addBoxMakerEvent(transaction, event, boxMakerEvent);
            } else if (EventTypeMarketBox.isMarketBoxTakerEvent(event)) {
                MarketBoxTakerEvent boxTakerEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), MarketBoxTakerEvent.class);
                marketService.addBoxTakerEvent(transaction, event, boxTakerEvent);
            } else if (EventTypeMarketBox.isMarketBoxBidEvent(event)) {
                MarketBoxBidEvent boxBidEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), MarketBoxBidEvent.class);
                marketService.addBoxBidEvent(transaction, event, boxBidEvent);
            } else if (EventTypeMarketBox.isMarketBoxCancelEvent(event)) {
                MarketBoxCancelEvent boxCancelEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), MarketBoxCancelEvent.class);
                marketService.addBoxCancelEvent(transaction, event, boxCancelEvent);
            } else if (EventTypeMarketNft.isMarketNftMakerEvent(event)) {
                MarketNftMakerEvent nftMakerEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), MarketNftMakerEvent.class);
                marketService.addNftMakerEvent(transaction, event, nftMakerEvent);
            } else if (EventTypeMarketNft.isMarketNftTakerEvent(event)) {
                MarketNftTakerEvent nftTakerEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), MarketNftTakerEvent.class);
                marketService.addNftTakerEvent(transaction, event, nftTakerEvent);
            } else if (EventTypeMarketNft.isMarketNftBidEvent(event)) {
                MarketNftBidEvent nftBidEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), MarketNftBidEvent.class);
                marketService.addNftBidEvent(transaction, event, nftBidEvent);
            } else if (EventTypeMarketNft.isMarketNftCancelEvent(event)) {
                MarketNftCancelEvent nftCancelEvent = JSONObject.parseObject(JSONObject.toJSONString(event.getData()), MarketNftCancelEvent.class);
                marketService.addNftCancelEvent(transaction, event, nftCancelEvent);
            }
        }
    }

    boolean isMarketEvent(Event event) {
        return EventTypeMarketBox.isMarketBoxMakerEvent(event)
                || EventTypeMarketBox.isMarketBoxTakerEvent(event)
                || EventTypeMarketBox.isMarketBoxBidEvent(event)
                || EventTypeMarketBox.isMarketBoxCancelEvent(event)
                || EventTypeMarketNft.isMarketNftMakerEvent(event)
                || EventTypeMarketNft.isMarketNftTakerEvent(event)
                || EventTypeMarketNft.isMarketNftBidEvent(event)
                || EventTypeMarketNft.isMarketNftCancelEvent(event);
    }

    boolean isNftEvent(Event event) {
        return EventTypeNft.isNftCreateTokenDataEvent(event)
                || EventTypeNft.isNftWithdrawEvent(event)
                || EventTypeNft.isNftDepositEvent(event)
                || EventTypeNft.isNftBurnTokenEvent(event);

    }

}