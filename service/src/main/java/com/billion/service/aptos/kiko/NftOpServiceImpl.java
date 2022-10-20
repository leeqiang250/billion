package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.TransactionPayload;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftComposeMapper;
import com.billion.dao.aptos.kiko.NftSplitMapper;
import com.billion.model.entity.NftCompose;
import com.billion.model.entity.NftSplit;
import com.billion.model.enums.TransactionStatus;
import com.billion.model.event.NftComposeEvent;
import com.billion.model.event.NftSplitEvent;
import com.billion.service.aptos.AptosService;
import com.billion.service.aptos.ContextService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.billion.model.constant.RequestPath.EMPTY;

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
        nftSplit.setTransactionStatus_(TransactionStatus.STATUS_1_READY);
        nftSplit.setTransactionHash(EMPTY);
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
        nftCompose.setTransactionStatus_(TransactionStatus.STATUS_1_READY);
        nftCompose.setTransactionHash(EMPTY);
        nftComposeMapper.insert(nftCompose);

        if (nftComposeEvent.isExecute()) {
            //TODO renjian
            //合成记录
        } else {

        }

        return true;
    }

    @Override
    public boolean execute() {
        while (true) {
            if (!nftSplit()) {
                break;
            }
        }

        while (true) {
            if (!nftCompose()) {
                break;
            }
        }

        return true;
    }

    boolean nftSplit() {
        QueryWrapper<NftSplit> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftSplit::getIsExecute, Boolean.FALSE);
        wrapper.lambda().eq(NftSplit::getTransactionStatus, TransactionStatus.STATUS_1_READY.getCode());
        var nftSplit = nftSplitMapper.selectOne(wrapper);
        if (Objects.isNull(nftSplit)) {
            return false;
        }

        List<String> propertyKeys = new ArrayList<>();

        TransactionPayload transactionPayload = TransactionPayload.builder()
                .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                .function(ContextService.getKikoOwner() + "::op_nft::split_step2")
                .arguments(List.of(
                        nftSplit.getOrderId(),
                        propertyKeys
                ))
                .typeArguments(List.of())
                .build();

        var response = AptosService.getAptosClient().requestSubmitTransaction(
                ContextService.getKikoOwner(),
                transactionPayload);
        if (response.isValid()) {
            nftSplit.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            nftSplit.setTransactionHash(EMPTY);
            nftSplitMapper.updateById(nftSplit);

            return false;
        }

        if (!AptosService.checkTransaction(response.getData().getHash())) {
            nftSplit.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            nftSplit.setTransactionHash(response.getData().getHash());
            nftSplitMapper.updateById(nftSplit);

            return false;
        }

        nftSplit.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        nftSplit.setTransactionHash(response.getData().getHash());
        nftSplit.setIsExecute(Boolean.TRUE);

        nftSplitMapper.updateById(nftSplit);

        return true;
    }

    boolean nftCompose() {
        QueryWrapper<NftCompose> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftCompose::getIsExecute, Boolean.FALSE);
        wrapper.lambda().eq(NftCompose::getTransactionStatus, TransactionStatus.STATUS_1_READY.getCode());
        var nftCompose = nftComposeMapper.selectOne(wrapper);
        if (Objects.isNull(nftCompose)) {
            return false;
        }

        List<String> propertyKeys = new ArrayList<>();

        TransactionPayload transactionPayload = TransactionPayload.builder()
                .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                .function(ContextService.getKikoOwner() + "::op_nft::compose_step2")
                .arguments(List.of(
                        nftCompose.getOrderId()
                        //uri
                ))
                .typeArguments(List.of())
                .build();

        var response = AptosService.getAptosClient().requestSubmitTransaction(
                ContextService.getKikoOwner(),
                transactionPayload);
        if (response.isValid()) {
            nftCompose.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            nftCompose.setTransactionHash(EMPTY);
            nftComposeMapper.updateById(nftCompose);

            return false;
        }

        if (!AptosService.checkTransaction(response.getData().getHash())) {
            nftCompose.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            nftCompose.setTransactionHash(response.getData().getHash());
            nftComposeMapper.updateById(nftCompose);

            return false;
        }

        nftCompose.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        nftCompose.setTransactionHash(response.getData().getHash());
        nftCompose.setIsExecute(Boolean.TRUE);

        nftComposeMapper.updateById(nftCompose);

        return true;
    }

}