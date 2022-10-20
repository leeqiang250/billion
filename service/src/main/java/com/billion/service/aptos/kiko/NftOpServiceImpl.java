package com.billion.service.aptos.kiko;

import com.billion.dao.aptos.kiko.NftComposeMapper;
import com.billion.dao.aptos.kiko.NftSplitMapper;
import com.billion.model.entity.NftCompose;
import com.billion.model.entity.NftSplit;
import com.billion.model.event.NftComposeEvent;
import com.billion.model.event.NftSplitEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author liqiang
 */
@Service
public class NftOpServiceImpl implements NftOpService {

    @Resource
    NftSplitMapper nftSplitMapper;

    @Resource
    NftComposeMapper nftComposeMapper;

    @Override
    public boolean addNftSplitEvent(NftSplitEvent nftSplitEvent) {
        var nftSplit = NftSplit.builder()
                .orderId(nftSplitEvent.getOrderId())
                .isExecute(nftSplitEvent.isExecute())
                .owner(nftSplitEvent.getOwner())
                .sourceUri(nftSplitEvent.getSourceUri())
                .build();
        nftSplitMapper.insert(nftSplit);

        if (nftSplitEvent.isExecute()) {
            //TODO renjian
            //分解记录
        } else {

        }

        return true;
    }

    @Override
    public boolean addNftComposeEvent(NftComposeEvent nftComposeEvent) {
        var nftCompose = NftCompose.builder()
                .orderId(nftComposeEvent.getOrderId())
                .isExecute(nftComposeEvent.isExecute())
                .owner(nftComposeEvent.getOwner())
                .name(nftComposeEvent.getName())
                .description(nftComposeEvent.getDescription())
                .build();
        nftComposeMapper.insert(nftCompose);

        if (nftComposeEvent.isExecute()) {
            //TODO renjian
            //合成记录
        } else {

        }

        return true;
    }

}