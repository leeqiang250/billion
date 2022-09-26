package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.TransactionPayload;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftTransferMapper;
import com.billion.model.entity.NftTransfer;
import com.billion.model.enums.TransactionStatus;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
import com.billion.service.aptos.ContextService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

/**
 * @author liqiang
 */
@Service
public class NftTransferServiceImpl extends AbstractCacheService<NftTransferMapper, NftTransfer> implements NftTransferService {

    @PostConstruct
    public NftTransfer fds() {
        QueryWrapper<NftTransfer> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftTransfer::getTransactionStatus, TransactionStatus.STATUS_1_READY.getCode());
        wrapper.lambda().orderByAsc(NftTransfer::getId);
        var nftTransfer = super.getOne(wrapper, false);
        if (Objects.isNull(nftTransfer)) {
            return null;
        }

        nftTransfer.setTransactionStatus_(TransactionStatus.STATUS_2_ING);
        super.getBaseMapper().updateById(nftTransfer);

        TransactionPayload transactionPayload = TransactionPayload.builder()
                .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                .function(ContextService.getTokenOwnerAddress() + "::token_v1::transfer_v2")
                .arguments(List.of(
                        nftTransfer.getCreator(),
                        nftTransfer.getCollection(),
                        nftTransfer.getName(),
                        "0",
                        nftTransfer.getTo(),
                        "1"
                ))
                .typeArguments(List.of())
                .build();

        var transaction = AptosService.getAptosClient().requestSubmitTransaction(
                nftTransfer.getFrom(),
                transactionPayload);

        if (AptosService.checkTransaction(transaction.getHash())) {
            nftTransfer.setTransactionHash(transaction.getHash());
            nftTransfer.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        } else {
            nftTransfer.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
        }
        super.getBaseMapper().updateById(nftTransfer);

        return nftTransfer;
    }

}