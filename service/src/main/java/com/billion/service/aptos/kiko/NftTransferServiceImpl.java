package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.TransactionPayload;
import com.aptos.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftTransferMapper;
import com.billion.model.entity.NftTransfer;
import com.billion.model.enums.TransactionStatus;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
import com.billion.service.aptos.ContextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.billion.model.constant.RequestPath.EMPTY;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class NftTransferServiceImpl extends AbstractCacheService<NftTransferMapper, NftTransfer> implements NftTransferService {

    @javax.annotation.Resource
    HandleService handleService;

    @Override
    public NftTransfer transfer() {
        QueryWrapper<NftTransfer> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftTransfer::getTransactionStatus, TransactionStatus.STATUS_1_READY.getCode());
        wrapper.lambda().orderByAsc(NftTransfer::getId);
        var nftTransfer = super.getOne(wrapper, false);
        if (Objects.isNull(nftTransfer)) {
            return null;
        }

        var accountTokenStore = handleService.getAccountTokenStore(nftTransfer.getTo());
        if (Objects.isNull(accountTokenStore)
                || !accountTokenStore.getData().getData().isDirectTransfer()
        ) {
            nftTransfer.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            nftTransfer.setTransactionHash(EMPTY);
            nftTransfer.setDescription("Account TokenStore non-existent or direct_transfer is false");
            super.updateById(nftTransfer);

            this.log(nftTransfer);

            return null;
        }

        var handle = handleService.getByAccount(nftTransfer.getFrom());
        if (Objects.isNull(handle)
                || StringUtils.isEmpty(handle.getTokenStoreTokensHandle())) {
            nftTransfer.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            nftTransfer.setTransactionHash(EMPTY);
            nftTransfer.setDescription("TokenStore Tokens Handle is null");
            super.updateById(nftTransfer);

            this.log(nftTransfer);

            return null;
        }

        var tableTokenDataResponse = AptosService.getAptosClient().requestTableToken(
                handle.getTokenStoreTokensHandle(),
                nftTransfer.getCreator(),
                nftTransfer.getCollection(),
                nftTransfer.getName()
        );

        if (tableTokenDataResponse.isValid()) {
            nftTransfer.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            nftTransfer.setTransactionHash(EMPTY);
            nftTransfer.setDescription(tableTokenDataResponse.getErrorCode());
            super.updateById(nftTransfer);

            this.log(nftTransfer);

            return null;
        }

        nftTransfer.setTransactionStatus_(TransactionStatus.STATUS_2_ING);
        super.updateById(nftTransfer);

        TransactionPayload transactionPayload = TransactionPayload.builder()
                .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                .function(ContextService.getKikoOwner() + "::token_v1::transfer_v2")
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

        var transactionResponse = AptosService.getAptosClient().requestSubmitTransaction(
                nftTransfer.getFrom(),
                transactionPayload);
        if (transactionResponse.isValid()) {
            nftTransfer.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            nftTransfer.setTransactionHash(EMPTY);
            nftTransfer.setDescription(transactionResponse.getErrorCode());
            super.updateById(nftTransfer);

            this.log(nftTransfer);

            return null;
        }

        nftTransfer.setTransactionHash(transactionResponse.getData().getHash());

        transactionResponse = AptosService.getTransaction(transactionResponse.getData().getHash());
        if (transactionResponse.isValid()) {
            nftTransfer.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            nftTransfer.setTransactionHash(EMPTY);
        } else {
            if (transactionResponse.getData().isSuccess()) {
                nftTransfer.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
                nftTransfer.setDescription(EMPTY);
            } else {
                nftTransfer.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
                nftTransfer.setTransactionHash(EMPTY);
                nftTransfer.setDescription(transactionResponse.getData().getVmStatus());
            }
        }
        super.updateById(nftTransfer);

        this.log(nftTransfer);

        return nftTransfer;
    }

    void log(NftTransfer nftTransfer) {
        log.info("------------------------------------------------------------------------------------------------");
        log.info("token transfer {}", nftTransfer);
        log.info("------------------------------------------------------------------------------------------------");
    }

}