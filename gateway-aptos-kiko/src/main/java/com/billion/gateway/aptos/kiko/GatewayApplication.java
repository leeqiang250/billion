package com.billion.gateway.aptos.kiko;

import com.aptos.utils.Hex;
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
        System.out.println(Hex.encode("name"));
        System.out.println(Hex.encode("name1"));
        System.out.println(Hex.encode(Hex.encode("name")));
        System.out.println(Hex.encode(Hex.encode("name1")));
        SpringApplication.run(GatewayApplication.class, args);
    }

}