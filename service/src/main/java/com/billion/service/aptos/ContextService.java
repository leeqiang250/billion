package com.billion.service.aptos;

import com.billion.model.service.CacheTsType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
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

    @Value("${spring.profiles.active}")
    String env_;

    @Getter
    static String env;

    @Value("${spring.application.name}")
    String applicationName_;

    @Getter
    static String applicationName;


    @Value("${cache.long}")
    long cacheLong_;

    @Value("${cache.middle}")
    long cacheMiddle_;

    @Value("${cache.short}")
    long cacheShort_;

    @Getter
    static Map<CacheTsType, Duration> cacheTsTypeDurationMap = new HashMap<>(CacheTsType.values().length);

    @Value("${kiko.stc.image-group-api}")
    String kikoStcImageGroupApi_;

    @Getter
    static String kikoStcImageGroupApi;

    @Value("${kiko.stc.image-info-api}")
    String kikoStcImageInfoApi_;

    @Getter
    static String kikoStcImageInfoApi;

    @Value("${kiko.host}")
    String kikoHost_;

    @Getter
    static String kikoHost;

    @Value("${aptos.host}")
    String aptosHost_;

    @Getter
    static String aptosHost;

    @PostConstruct
    public void init() {
        ContextService.env = this.env_;
        ContextService.applicationName = this.applicationName_;
        ContextService.cacheTsTypeDurationMap.put(CacheTsType.CACHE_TS_SHORT, Duration.ofSeconds(cacheShort_));
        ContextService.cacheTsTypeDurationMap.put(CacheTsType.CACHE_TS_MIDDLE, Duration.ofSeconds(cacheMiddle_));
        ContextService.cacheTsTypeDurationMap.put(CacheTsType.CACHE_TS_LONG, Duration.ofSeconds(cacheLong_));
        Stream.of(CacheTsType.values()).forEach(e -> Assert.notNull(ContextService.cacheTsTypeDurationMap.get(e), "missing cache ts type"));
        ContextService.kikoStcImageGroupApi = this.kikoStcImageGroupApi_;
        ContextService.kikoStcImageInfoApi = this.kikoStcImageInfoApi_;
        ContextService.kikoHost = this.kikoHost_;
        ContextService.aptosHost = this.aptosHost_;
    }

}