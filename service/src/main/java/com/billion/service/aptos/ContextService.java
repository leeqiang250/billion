package com.billion.service.aptos;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

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

    @Getter
    static long cacheLong;

    @Value("${cache.middle}")
    long cacheMiddle_;

    @Getter
    static long cacheMiddle;

    @Value("${cache.short}")
    long cacheShort_;

    @Getter
    static long cacheShort;

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
        ContextService.cacheLong = this.cacheLong_;
        ContextService.cacheMiddle = this.cacheMiddle_;
        ContextService.cacheShort = this.cacheShort_;
        ContextService.kikoHost = this.kikoHost_;
        ContextService.aptosHost = this.aptosHost_;
    }

}