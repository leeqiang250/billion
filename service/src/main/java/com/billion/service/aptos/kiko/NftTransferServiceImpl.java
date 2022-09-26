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

import java.util.List;
import java.util.Objects;

/**
 * @author liqiang
 */
@Service
public class NftTransferServiceImpl extends AbstractCacheService<NftTransferMapper, NftTransfer> implements NftTransferService {

    @javax.annotation.Resource
    HandleService handleService;

    @Override
    public NftTransfer dispatch() {
        QueryWrapper<NftTransfer> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftTransfer::getTransactionStatus, TransactionStatus.STATUS_1_READY.getCode());
        wrapper.lambda().orderByAsc(NftTransfer::getId);
        var nftTransfer = super.getOne(wrapper, false);
        if (Objects.isNull(nftTransfer)) {
            return null;
        }

        var accountTokenStore = handleService.getAccountTokenStore(nftTransfer.getTo());
        if (Objects.isNull(accountTokenStore)
                || !accountTokenStore.getData().isDirectTransfer()
        ) {
            nftTransfer.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            nftTransfer.setDescription("Account TokenStore non-existent or direct_transfer is false");
            super.updateById(nftTransfer);
            return null;
        }

        handleService.getByAccount(nftTransfer.getFrom());


//        AptosService.getAptosClient().aaaa(
//                "",
//                nftTransfer.getCreator(),
//                nftTransfer.getCollection(),
//                nftTransfer.getName()
//        );
        //检查资源是否存在

        nftTransfer.setTransactionStatus_(TransactionStatus.STATUS_2_ING);
        super.updateById(nftTransfer);

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
        nftTransfer.setTransactionHash(transaction.getHash());

        transaction = AptosService.getTransaction(transaction.getHash());
        if (Objects.isNull(transaction)) {
            nftTransfer.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
        } else {
            if (transaction.isSuccess()) {
                nftTransfer.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
            } else {
                nftTransfer.setDescription(transaction.getVmStatus());
                nftTransfer.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            }
        }
        super.updateById(nftTransfer);

        return nftTransfer;
    }

}