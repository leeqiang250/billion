package com.billion.gateway.aptos.kiko;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author liqiang
 */
@Slf4j
@SpringBootApplication
@MapperScan({"com.billion.dao"})
@ComponentScan({"com.billion.service", "com.billion.model", "com.billion.gateway"})
public class GatewayApplication {

    public static void main(String[] args) {
        log.info("------------------------------------------------------------------------------------------------");
        for (String arg : args) {
            log.info(arg);
        }
        log.info("------------------------------------------------------------------------------------------------");
        SpringApplication.run(GatewayApplication.class, args);
    }

}