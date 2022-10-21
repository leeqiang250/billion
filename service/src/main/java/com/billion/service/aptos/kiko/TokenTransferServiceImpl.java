package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Resource;
import com.aptos.request.v1.model.TransactionPayload;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.TokenTransferMapper;
import com.billion.model.entity.TokenTransfer;
import com.billion.model.enums.TransactionStatus;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
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
public class TokenTransferServiceImpl extends AbstractCacheService<TokenTransferMapper, TokenTransfer> implements TokenTransferService {

    @Override
    public TokenTransfer transfer() {
        QueryWrapper<TokenTransfer> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TokenTransfer::getTransactionStatus, TransactionStatus.STATUS_1_READY.getCode());
        wrapper.lambda().orderByAsc(TokenTransfer::getId);
        var tokenTransfer = super.getOne(wrapper, false);
        if (Objects.isNull(tokenTransfer)) {
            return null;
        }

        //TODO 资源检查
//        var accountTokenStore = handleService.getAccountTokenStore(nftTransfer.getTo());
//        if (Objects.isNull(accountTokenStore)
//                || !accountTokenStore.getData().getData().isDirectTransfer()
//        ) {
//            nftTransfer.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
//            nftTransfer.setDescription("Account TokenStore non-existent or direct_transfer is false");
//            super.updateById(nftTransfer);
//            return null;
//        }
//
//        var handle = handleService.getByAccount(nftTransfer.getFrom());
//        if (Objects.isNull(handle)
//                || StringUtils.isEmpty(handle.getTokenStoreTokensHandle())) {
//            nftTransfer.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
//            nftTransfer.setDescription("TokenStore Tokens Handle is null");
//            super.updateById(nftTransfer);
//            return null;
//        }
//
//        var tableTokenDataResponse = AptosService.getAptosClient().requestTableToken(
//                handle.getTokenStoreTokensHandle(),
//                nftTransfer.getCreator(),
//                nftTransfer.getCollection(),
//                nftTransfer.getName()
//        );
//
//        if (tableTokenDataResponse.isValid()) {
//            nftTransfer.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
//            nftTransfer.setDescription(tableTokenDataResponse.getErrorCode());
//            super.updateById(nftTransfer);
//            return null;
//        }

        tokenTransfer.setTransactionStatus_(TransactionStatus.STATUS_2_ING);
        super.updateById(tokenTransfer);

        var resource = Resource.builder()
                .moduleAddress(tokenTransfer.getModuleAddress())
                .moduleName(tokenTransfer.getModuleName())
                .resourceName(tokenTransfer.getResourceName())
                .build();

        TransactionPayload transactionPayload = TransactionPayload.builder()
                .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                .function("0x1::coin::transfer")
                .arguments(List.of(
                        tokenTransfer.getTo(),
                        tokenTransfer.getAmount()
                ))
                .typeArguments(List.of(resource.resourceTag()))
                .build();

        var transactionResponse = AptosService.getAptosClient().requestSubmitTransaction(
                tokenTransfer.getFrom(),
                transactionPayload);
        if (transactionResponse.isValid()) {
            tokenTransfer.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            tokenTransfer.setTransactionHash(EMPTY);
            tokenTransfer.setDescription(transactionResponse.getErrorCode());
            super.updateById(tokenTransfer);

            this.log(tokenTransfer);

            return null;
        }

        tokenTransfer.setTransactionHash(transactionResponse.getData().getHash());

        transactionResponse = AptosService.getTransaction(transactionResponse.getData().getHash());
        if (transactionResponse.isValid()) {
            tokenTransfer.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
        } else {
            if (transactionResponse.getData().isSuccess()) {
                tokenTransfer.setDescription(EMPTY);
                tokenTransfer.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
            } else {
                tokenTransfer.setDescription(transactionResponse.getData().getVmStatus());
                tokenTransfer.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            }
        }
        super.updateById(tokenTransfer);

        this.log(tokenTransfer);

        return tokenTransfer;
    }

    void log(TokenTransfer tokenTransfer){
        log.info("------------------------------------------------------------------------------------------------");
        log.info("token transfer[{}]", tokenTransfer);
        log.info("------------------------------------------------------------------------------------------------");
    }

}