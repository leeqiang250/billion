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

    @Getter
    @Value("${spring.profiles.active}")
    String env_;

    @Getter
    static String env;

    @Getter
    static boolean isProd;

    @Getter
    @Value("${spring.application.name}")
    String applicationName_;

    @Getter
    static String applicationName;

    @Value("${aptos.host}")
    String aptosHost_;

    @Getter
    static String aptosHost;

    @Getter
    @Value("${kiko.host}")
    String kikoHost_;

    @Getter
    static String kikoHost;

    @PostConstruct
    public void init() {
        ContextService.env = this.env_;
        ContextService.isProd = "prod".equalsIgnoreCase(this.env_);
        ContextService.applicationName = this.applicationName_;
        ContextService.aptosHost = this.aptosHost_;
        ContextService.kikoHost = this.kikoHost_;
    }

}