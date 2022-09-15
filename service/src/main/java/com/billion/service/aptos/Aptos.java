package com.billion.service.aptos;

import com.aptos.utils.AptosClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class Aptos {

    @Value("${aptos.host}")
    private String aptosHost;

    @Getter
    static AptosClient aptosClient;

    @PostConstruct
    public void init() {
        Aptos.aptosClient = new AptosClient(this.aptosHost);
        log.info("Aptos{}", Aptos.aptosClient.requestNode());
    }

}