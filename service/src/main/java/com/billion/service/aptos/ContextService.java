package com.billion.service.aptos;

import com.billion.model.enums.CacheTsType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class ContextService {

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

    @Value("${kiko.host}")
    String pKikoHost;

    @Getter
    static String kikoHost;

    @Value("${kiko.account.tokenOwner.address}")
    String pTokenOwnerAddress;

    @Getter
    static String tokenOwnerAddress;

    @Value("${kiko.account.nftOwner.address}")
    String pNftOwnerAddress;

    @Getter
    static String nftOwnerAddress;

    @Value("${kiko.event}")
    String pEvent;

    @Getter
    static Set<String> event;

    @Value("${aptos.host}")
    String pAptosHost;

    @Getter
    static String aptosHost;

    @PostConstruct
    public void init() {
        ContextService.env = this.pEnv;
        ContextService.applicationName = this.pApplicationName;
        ContextService.cacheTsTypeDurationMap.put(CacheTsType.SHORT, Duration.ofSeconds(pCacheShort));
        ContextService.cacheTsTypeDurationMap.put(CacheTsType.MIDDLE, Duration.ofSeconds(pCacheMiddle));
        ContextService.cacheTsTypeDurationMap.put(CacheTsType.LONG, Duration.ofSeconds(pCacheLong));
        Stream.of(CacheTsType.values()).forEach(e -> Assert.notNull(ContextService.cacheTsTypeDurationMap.get(e), "missing cache ts type"));
        ContextService.kikoStcImageGroupApi = this.pKikoStcImageGroupApi;
        ContextService.kikoStcImageInfoApi = this.pKikoStcImageInfoApi;
        ContextService.kikoHost = this.pKikoHost;
        ContextService.tokenOwnerAddress = this.pTokenOwnerAddress;
        ContextService.nftOwnerAddress = this.pNftOwnerAddress;
        ContextService.event = Stream.of(this.pEvent.split(",")).collect(Collectors.toSet());
        ContextService.aptosHost = this.pAptosHost;
    }

}