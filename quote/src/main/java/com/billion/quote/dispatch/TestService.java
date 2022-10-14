package com.billion.quote.dispatch;

import com.billion.service.aptos.kiko.MintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * @author liqiang
 */
@Slf4j
@Component
@Configuration
@EnableScheduling
public class TestService implements Serializable {

    @Resource
    MintService mintService;

    @Scheduled(cron = "*/2 * * * * ?")
    void dispatch() {
        //mintService.initialize("39");
        //mintService.initialize("40");
        //mintService.initialize("41");
        //mintService.initialize("42");
        //mintService.initialize("43");
    }

}