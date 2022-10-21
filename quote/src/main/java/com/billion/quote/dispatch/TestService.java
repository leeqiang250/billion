package com.billion.quote.dispatch;

import com.billion.service.aptos.kiko.BoxGroupService;
import com.billion.service.aptos.kiko.MintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
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

    @Resource
    BoxGroupService boxGroupService;

    //@Scheduled(fixedDelay = 1000)
    void dispatch() {
        var list = boxGroupService.list();
        list.forEach(boxGroup -> mintService.initialize(boxGroup.getId()));
    }

}