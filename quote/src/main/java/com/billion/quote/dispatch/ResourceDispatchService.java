package com.billion.quote.dispatch;

import com.billion.model.entity.NftTransfer;
import com.billion.model.entity.TokenTransfer;
import com.billion.service.aptos.kiko.NftTransferService;
import com.billion.service.aptos.kiko.TokenTransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author liqiang
 */
@Slf4j
@Component
@Configuration
@EnableScheduling
public class ResourceDispatchService implements Serializable {

    @Resource
    TokenTransferService tokenTransferService;

    @Resource
    NftTransferService nftTransferService;

    //@Scheduled(cron = "*/3 * * * * ?")
    void dispatch() {
        TokenTransfer tokenTransfer = TokenTransfer.builder().build();
        NftTransfer nftTransfer = NftTransfer.builder().build();

        while (Objects.nonNull(tokenTransfer)
                || Objects.nonNull(nftTransfer)
        ) {
            tokenTransfer = tokenTransferService.transfer();
            nftTransfer = nftTransferService.transfer();
        }
    }

}