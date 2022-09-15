package com.billion.service.aptos;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class ContextService {

    @Value("${spring.profiles.active}")
    String env;

    @Getter
    @Value("${spring.application.name}")
    String applicationName;

    @Getter
    @Value("${aptos.host}")
    String aptosHost;

    @Getter
    @Value("${kiko.host}")
    String kikoHost;

    public boolean isProd() {
        return "prod".equalsIgnoreCase(env);
    }

}