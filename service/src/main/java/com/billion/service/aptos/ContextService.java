package com.billion.service.aptos;

import com.billion.model.enums.CacheTsType;
import com.billion.model.enums.Contract;
import com.billion.model.event.*;
import com.billion.service.aptos.kiko.ContractService;
import com.billion.service.aptos.kiko.InitService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class ContextService {

    @Resource
    ContractService contractService;

    @Value("${spring.profiles.active}")
    String pEnv;

    @Getter
    static String env;

    @Value("${spring.application.name}")
    String pApplicationName;

    @Getter
    static String applicationName;

    @Value("${cache.long}")
    long pCacheLong;

    @Value("${cache.middle}")
    long pCacheMiddle;

    @Value("${cache.short}")
    long pCacheShort;

    @Getter
    static Map<CacheTsType, Duration> cacheTsTypeDurationMap = new HashMap<>(CacheTsType.values().length);

    @Value("${kiko.stc.image-group-api}")
    String pKikoStcImageGroupApi;

    @Getter
    static String kikoStcImageGroupApi;

    @Value("${kiko.stc.image-info-api}")
    String pKikoStcImageInfoApi;

    @Getter
    static String kikoStcImageInfoApi;

    @Value("${kiko.nft-image-path}")
    String pKikoNftImagePath;

    @Getter
    static String kikoNftImagePath;

    @Getter
    static String kikoPath;

    @Value("${kiko.host}")
    String pKikoHost;

    @Getter
    static String kikoHost;

    @Getter
    static String kikoOwner;

    @Value("${kiko.nft.compose_fee}")
    String pNftComposeFee;

    @Getter
    static String nftComposeFee;

    @Value("${aptos.host}")
    String pAptosHost;

    @Getter
    static String aptosHost;

    @Resource
    InitService initService;

    @Resource
    AptosService aptosService;

    @PostConstruct
    public void init() {
        ContextService.env = this.pEnv;

        //initService.initialize();

        ContextService.applicationName = this.pApplicationName;
        ContextService.cacheTsTypeDurationMap.put(CacheTsType.SHORT, Duration.ofSeconds(pCacheShort));
        ContextService.cacheTsTypeDurationMap.put(CacheTsType.MIDDLE, Duration.ofSeconds(pCacheMiddle));
        ContextService.cacheTsTypeDurationMap.put(CacheTsType.LONG, Duration.ofSeconds(pCacheLong));
        Stream.of(CacheTsType.values()).forEach(e -> Assert.notNull(ContextService.cacheTsTypeDurationMap.get(e), "missing cache ts type"));
        ContextService.kikoStcImageGroupApi = this.pKikoStcImageGroupApi;
        ContextService.kikoStcImageInfoApi = this.pKikoStcImageInfoApi;
        ContextService.kikoNftImagePath = this.pKikoNftImagePath;
        ContextService.kikoNftImagePath = "/Users/liqiang/kiko/image/";
        ContextService.kikoPath = "/Users/liqiang/kiko/image/";
        ContextService.kikoHost = this.pKikoHost;
        ContextService.kikoOwner = contractService.getByName(Contract.PRIMARY_MARKET.getCode()).getModuleAddress();
        ContextService.nftComposeFee = this.pNftComposeFee;
        ContextService.aptosHost = this.pAptosHost;
        NftComposeEvent.EVENT_NAME = ContextService.kikoOwner + NftComposeEvent.EVENT_NAME;
        NftSplitEvent.EVENT_NAME = ContextService.kikoOwner + NftSplitEvent.EVENT_NAME;
        OpenBoxEvent.EVENT_NAME = ContextService.kikoOwner + OpenBoxEvent.EVENT_NAME;

        MarketBidEvent.EVENT_NAME = ContextService.kikoOwner + MarketBidEvent.EVENT_NAME;
        MarketCancelEvent.EVENT_NAME = ContextService.kikoOwner + MarketCancelEvent.EVENT_NAME;
        MarketMakerEvent.EVENT_NAME = ContextService.kikoOwner + MarketMakerEvent.EVENT_NAME;
        MarketTakerEvent.EVENT_NAME = ContextService.kikoOwner + MarketTakerEvent.EVENT_NAME;

        aptosService.initialize();
    }

}