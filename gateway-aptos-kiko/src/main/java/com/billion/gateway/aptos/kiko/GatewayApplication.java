package com.billion.gateway.aptos.kiko;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author liqiang
 */
@SpringBootApplication
@MapperScan({"com.billion.dao"})
@ComponentScan({"com.billion.service", "com.billion.model", "com.billion.gateway"})
public class GatewayApplication {

    public static void main(String[] args) {
        System.out.println(args.length);
        for (String arg : args) {
            System.out.println(arg);
        }
        SpringApplication.run(GatewayApplication.class, args);
    }

}