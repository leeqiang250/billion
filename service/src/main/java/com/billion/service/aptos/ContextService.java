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

    @Value("${kiko.account.commoner}")
    String pCommoner;

    @Getter
    static String commoner;

    @Value("${kiko.account.token_owner}")
    String pTokenOwner;

    @Getter
    static String tokenOwner;

    @Value("${kiko.account.nft_owner}")
    String pNftOwner;

    @Getter
    static String nftOwner;

    @Value("${kiko.account.marketer}")
    String pMarketer;

    @Getter
    static String marketer;

    @Getter
    static String address;

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
        ContextService.commoner = this.pCommoner;
        ContextService.tokenOwner = this.pTokenOwner;
        ContextService.nftOwner = this.pNftOwner;
        ContextService.marketer = this.pMarketer;
        ContextService.address = ContextService.commoner + "," +
                ContextService.tokenOwner + "," +
                ContextService.nftOwner + "," +
                ContextService.marketer;


        ContextService.aptosHost = this.pAptosHost;
    }

}