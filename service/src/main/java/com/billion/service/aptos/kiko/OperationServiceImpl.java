package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Event;
import com.aptos.request.v1.model.Transaction;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.OperationMapper;
import com.billion.model.dto.Context;
import com.billion.model.entity.Operation;
import com.billion.model.enums.Chain;
import com.billion.model.enums.OperationTraStateType;
import com.billion.model.enums.OperationType;
import com.billion.model.enums.TransactionStatus;
import com.billion.model.event.*;
import com.billion.service.aptos.AbstractCacheService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.billion.model.constant.RequestPath.EMPTY;

/**
 * @author jason
 */
@Service
public class OperationServiceImpl extends AbstractCacheService<OperationMapper, Operation> implements OperationService {

    @Override
    public Operation addBoxMakerOpt(Transaction transaction, Event event, BoxMakerEvent boxMakerEvent) {
        Operation operation = Operation.builder()
                .chain(Chain.APTOS.getCode())
                .maker(boxMakerEvent.getMaker())
                .bidder(EMPTY)
                .traType(boxMakerEvent.getType())
                .orderId(boxMakerEvent.getId())
                .type(OperationType.BOX_MAKER_EVENT.getType())
                .tokenId(event.getType().split("<")[1].split(">")[0].split(",")[0].trim())
                .tokenAmount(Long.valueOf(boxMakerEvent.getAmount()))
                .price(boxMakerEvent.getPrice())
                .bidToken(EMPTY)
                .bidTokenAmount(EMPTY)
                .state(OperationTraStateType.SELL.getType())
                .ts(boxMakerEvent.getTs())
                .build();
        operation.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        operation.setTransactionHash(transaction.getHash());
        this.save(operation);
        return operation;
    }

    @Override
    public Operation addBoxTakerOpt(Transaction transaction, Event event, BoxTakerEvent boxTakerEvent) {
        Operation operation = Operation.builder()
                .chain(Chain.APTOS.getCode())
                .maker(boxTakerEvent.getMaker())
                .bidder(boxTakerEvent.getBidder())
                .traType(boxTakerEvent.getType())
                .orderId(boxTakerEvent.getId())
                .type(OperationType.BOX_TAKER_EVENT.getType())
                .tokenId(event.getType().split("<")[1].split(">")[0].split(",")[0].trim())
                .tokenAmount(Long.valueOf(boxTakerEvent.getAmount()))
                .price(boxTakerEvent.getPrice())
                .bidToken(event.getType().split("<")[1].split(">")[0].split(",")[1].trim())
                .bidTokenAmount(boxTakerEvent.getFinalPrice())
                .state(OperationTraStateType.ACCEPT_PRICE.getType())
                .ts(boxTakerEvent.getTs())
                .build();
        operation.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        operation.setTransactionHash(transaction.getHash());
        this.save(operation);
        //更新售卖的option状态为Done，完成
        QueryWrapper<Operation> operationQueryWrapper = new QueryWrapper<>();
        operationQueryWrapper.lambda().eq(Operation::getOrderId, boxTakerEvent.getId());
        operationQueryWrapper.lambda().eq(Operation::getState, OperationTraStateType.SELL.getType());
        Operation sellOperation = this.getOne(operationQueryWrapper);
        sellOperation.setState(OperationTraStateType.DONE.getType());
        this.updateById(sellOperation);

        return operation;
    }

    @Override
    public Operation addBoxBidOpt(Transaction transaction, Event event, BoxBidEvent boxBidEvent) {
        Operation operation = Operation.builder()
                .chain(Chain.APTOS.getCode())
                .maker(boxBidEvent.getMaker())
                .bidder(boxBidEvent.getBidder())
                .traType(boxBidEvent.getType())
                .orderId(boxBidEvent.getId())
                .type(OperationType.BOX_BID_EVENT.getType())
                .tokenId(event.getType().split("<")[1].split(">")[0].split(",")[0].trim())
                .tokenAmount(Long.valueOf(boxBidEvent.getAmount()))
                .price(boxBidEvent.getBidPrice())
                .bidToken(event.getType().split("<")[1].split(">")[0].split(",")[1].trim())
                .bidTokenAmount(boxBidEvent.getBidPrice())
                .state(OperationTraStateType.HIGHEST_PRICE.getType())
                .ts(boxBidEvent.getTs())
                .build();
        operation.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        operation.setTransactionHash(transaction.getHash());
        this.save(operation);
        //更新上一个报价状态为over_price
        QueryWrapper<Operation> operationQueryWrapper = new QueryWrapper<>();
        operationQueryWrapper.lambda().eq(Operation::getOrderId, boxBidEvent.getId());
        operationQueryWrapper.lambda().eq(Operation::getState, OperationTraStateType.HIGHEST_PRICE.getType());
        Operation bidOperation = this.getOne(operationQueryWrapper);
        if (!Objects.isNull(bidOperation)) {
            bidOperation.setState(OperationTraStateType.OVER_PRICE.getType());
            this.updateById(bidOperation);
        }
        return operation;
    }

    @Override
    public Operation addBoxCancelOpt(Transaction transaction, Event event, BoxCancelEvent boxCancelEvent) {
        Operation operation = Operation.builder()
                .chain(Chain.APTOS.getCode())
                .maker(boxCancelEvent.getMaker())
                .bidder(boxCancelEvent.getBidder())
                .traType(boxCancelEvent.getType())
                .orderId(boxCancelEvent.getId())
                .type(OperationType.BOX_CANCLE_EVENT.getType())
                .tokenId(event.getType().split("<")[1].split(">")[0].split(",")[0].trim())
                .tokenAmount(Long.valueOf(boxCancelEvent.getAmount()))
                .price(boxCancelEvent.getPrice())
                .bidToken(event.getType().split("<")[1].split(">")[0].split(",")[1].trim())
                .bidTokenAmount(boxCancelEvent.getBidPrice())
                .state(OperationTraStateType.SELL_CANCEL.getType())
                .ts(boxCancelEvent.getTs())
                .build();
        operation.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        operation.setTransactionHash(transaction.getHash());
        this.save(operation);

        //更新售卖的option状态为cancle，取消
        QueryWrapper<Operation> operationQueryWrapper = new QueryWrapper<>();
        operationQueryWrapper.lambda().eq(Operation::getOrderId, boxCancelEvent.getId());
        operationQueryWrapper.lambda().eq(Operation::getState, OperationTraStateType.SELL.getType());
        Operation sellOperation = this.getOne(operationQueryWrapper);
        sellOperation.setState(OperationTraStateType.SELL_CANCEL.getType());
        this.updateById(sellOperation);

        return operation;
    }

    @Override
    public Operation addNftMintOpt(Transaction transaction, Event event, NftCreateTokenDataEvent nftCreateTokenDataEvent) {
        Operation operation = Operation.builder()
                .chain(Chain.APTOS.getCode())
                .maker(event.getGuid().getAccountAddress())
                .bidder(EMPTY)
                .traType(EMPTY)
                .orderId(EMPTY)
                .type(OperationType.NFT_MINT_EVENT.getType())
                .tokenId(nftCreateTokenDataEvent.getTokenId().getNftTokenIdKey())
                .tokenAmount(1L)
                .bidToken(EMPTY)
                .bidTokenAmount(EMPTY)
                .price(EMPTY)
                .state(EMPTY)
                .ts(transaction.getTimestampMillisecond())
                .build();
        operation.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        operation.setTransactionHash(transaction.getHash());
        this.save(operation);
        return operation;
    }

    @Override
    public Operation addNftWithdrawOpt(Transaction transaction, Event event, NftWithdrawEvent nftWithdrawEvent) {
        Operation operation = Operation.builder()
                .chain(Chain.APTOS.getCode())
                .maker(event.getGuid().getAccountAddress())
                .bidder(EMPTY)
                .traType(EMPTY)
                .orderId(EMPTY)
                .type(OperationType.NFT_WITHDRAW_EVENT.getType())
                .tokenId(nftWithdrawEvent.getId().getNftTokenIdKey())
                .tokenAmount(1L)
                .bidToken(EMPTY)
                .bidTokenAmount(EMPTY)
                .state(EMPTY)
                .price(EMPTY)
                .ts(transaction.getTimestampMillisecond())
                .build();
        operation.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        operation.setTransactionHash(transaction.getHash());
        this.save(operation);
        return operation;
    }

    @Override
    public Operation addNftDepositOpt(Transaction transaction, Event event, NftDepositEvent nftDepositEvent) {
        Operation operation = Operation.builder()
                .chain(Chain.APTOS.getCode())
                .maker(event.getGuid().getAccountAddress())
                .bidder(EMPTY)
                .traType(EMPTY)
                .orderId(EMPTY)
                .type(OperationType.NFT_DEPOSIT_EVENT.getType())
                .tokenId(nftDepositEvent.getId().getNftTokenIdKey())
                .tokenAmount(1L)
                .bidToken(EMPTY)
                .bidTokenAmount(EMPTY)
                .state(EMPTY)
                .price(EMPTY)
                .ts(transaction.getTimestampMillisecond())
                .build();
        operation.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        operation.setTransactionHash(transaction.getHash());
        this.save(operation);
        return operation;
    }

    @Override
    public Operation addNftBurnTokenOpt(Transaction transaction, Event event, NftBurnTokenEvent nftBurnTokenEvent) {
        Operation operation = Operation.builder()
                .chain(Chain.APTOS.getCode())
                .maker(event.getGuid().getAccountAddress())
                .bidder(EMPTY)
                .traType(EMPTY)
                .orderId(EMPTY)
                .type(OperationType.NFT_BURN_EVENT.getType())
                .tokenId(nftBurnTokenEvent.getId().getNftTokenIdKey())
                .tokenAmount(1L)
                .bidToken(EMPTY)
                .bidTokenAmount(EMPTY)
                .state(EMPTY)
                .price(EMPTY)
                .ts(transaction.getTimestampMillisecond())
                .build();
        operation.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        operation.setTransactionHash(transaction.getHash());
        this.save(operation);
        return operation;
    }

    @Override
    public Operation addNftMakerOpt(Transaction transaction, Event event, MarketMakerEvent nftMakerEvent) {
        Operation operation = Operation.builder()
                .chain(Chain.APTOS.getCode())
                .maker(nftMakerEvent.getMaker())
                .bidder(EMPTY)
                .traType(nftMakerEvent.getType())
                .orderId(nftMakerEvent.getId())
                .type(OperationType.NFT_MAKER_EVENT.getType())
                .tokenId(nftMakerEvent.getTokenId().getNftTokenIdKey())
                .tokenAmount(1L)
                .price(nftMakerEvent.getPrice())
                .bidToken(event.getType().split("<")[1].split(">")[0].split(",")[0].trim())
                .bidTokenAmount(EMPTY)
                .state(OperationTraStateType.SELL.getType())
                .ts(nftMakerEvent.getTs())
                .build();
        operation.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        operation.setTransactionHash(transaction.getHash());
        this.save(operation);
        return operation;
    }

    @Override
    public Operation addNftTakerOpt(Transaction transaction, Event event, MarketTakerEvent nftTakerEvent) {
        Operation operation = Operation.builder()
                .chain(Chain.APTOS.getCode())
                .maker(nftTakerEvent.getMaker())
                .bidder(nftTakerEvent.getBidder())
                .traType(nftTakerEvent.getType())
                .orderId(nftTakerEvent.getId())
                .type(OperationType.NFT_TAKER_EVENT.getType())
                .tokenId(nftTakerEvent.getTokenId().getNftTokenIdKey())
                .tokenAmount(Long.valueOf(nftTakerEvent.getAmount()))
                .bidToken(event.getType().split("<")[1].split(">")[0].split(",")[0].trim())
                .price(nftTakerEvent.getPrice())
                .bidTokenAmount(nftTakerEvent.getFinalPrice())
                .state(OperationTraStateType.ACCEPT_PRICE.getType())
                .ts(nftTakerEvent.getTs())
                .build();
        operation.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        operation.setTransactionHash(transaction.getHash());
        this.save(operation);

        //更新售卖的option状态为Done，完成
        QueryWrapper<Operation> operationQueryWrapper = new QueryWrapper<>();
        operationQueryWrapper.lambda().eq(Operation::getOrderId, nftTakerEvent.getId());
        operationQueryWrapper.lambda().eq(Operation::getState, OperationTraStateType.SELL.getType());
        Operation sellOperation = this.getOne(operationQueryWrapper);
        sellOperation.setState(OperationTraStateType.DONE.getType());
        this.updateById(sellOperation);

        return operation;
    }

    @Override
    public Operation addNftBidOpt(Transaction transaction, Event event, MarketBidEvent nftBidEvent) {
        Operation operation = Operation.builder()
                .chain(Chain.APTOS.getCode())
                .maker(nftBidEvent.getMaker())
                .bidder(nftBidEvent.getBidder())
                .traType(nftBidEvent.getType())
                .orderId(nftBidEvent.getId())
                .type(OperationType.NFT_BID_EVENT.getType())
                .tokenId(nftBidEvent.getTokenId().getNftTokenIdKey())
                .tokenAmount(Long.valueOf(nftBidEvent.getAmount()))
                .bidToken(event.getType().split("<")[1].split(">")[0].split(",")[0].trim())
                .price(nftBidEvent.getPrice())
                .bidTokenAmount(nftBidEvent.getBidPrice())
                .state(OperationTraStateType.HIGHEST_PRICE.getType())
                .ts(nftBidEvent.getTs())
                .build();
        operation.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        operation.setTransactionHash(transaction.getHash());
        this.save(operation);

        //更新上一个报价状态为over_price
        QueryWrapper<Operation> operationQueryWrapper = new QueryWrapper<>();
        operationQueryWrapper.lambda().eq(Operation::getOrderId, nftBidEvent.getId());
        operationQueryWrapper.lambda().eq(Operation::getState, OperationTraStateType.HIGHEST_PRICE.getType());
        Operation bidOperation = this.getOne(operationQueryWrapper);
        if (!Objects.isNull(bidOperation)) {
            bidOperation.setState(OperationTraStateType.OVER_PRICE.getType());
            this.updateById(bidOperation);
        }
        return operation;
    }

    @Override
    public Operation addNftCancelOpt(Transaction transaction, Event event, MarketCancelEvent nftCancelEvent) {
        Operation operation = Operation.builder()
                .chain(Chain.APTOS.getCode())
                .maker(nftCancelEvent.getMaker())
                .bidder(nftCancelEvent.getBidder())
                .traType(nftCancelEvent.getType())
                .orderId(nftCancelEvent.getId())
                .type(OperationType.NFT_CANCLE_EVENT.getType())
                .tokenId(nftCancelEvent.getTokenId().getNftTokenIdKey())
                .tokenAmount(Long.valueOf(nftCancelEvent.getAmount()))
                .bidToken(event.getType().split("<")[1].split(">")[0].split(",")[0].trim())
                .price(nftCancelEvent.getPrice())
                .bidTokenAmount(nftCancelEvent.getBidPrice())
                .state(OperationTraStateType.HIGHEST_PRICE.getType())
                .ts(nftCancelEvent.getTs())
                .build();
        operation.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        operation.setTransactionHash(transaction.getHash());
        this.save(operation);

        //更新售卖的option状态为cancle，取消
        QueryWrapper<Operation> operationQueryWrapper = new QueryWrapper<>();
        operationQueryWrapper.lambda().eq(Operation::getOrderId, nftCancelEvent.getId());
        operationQueryWrapper.lambda().eq(Operation::getState, OperationTraStateType.SELL.getType());
        Operation sellOperation = this.getOne(operationQueryWrapper);
        sellOperation.setState(OperationTraStateType.SELL_CANCEL.getType());
        this.updateById(sellOperation);

        return operation;
    }

    @Override
    public void addOpenBoxOpt(OpenBoxEvent openBoxEvent, Transaction transaction) {
        Operation operation = Operation.builder()
                .chain(Chain.APTOS.getCode())
                .maker(openBoxEvent.getOwner())
                .bidder(EMPTY)
                .traType(EMPTY)
                .orderId(EMPTY)
                .type(OperationType.BOX_OPEN_EVENT.getType())
                .tokenId(openBoxEvent.getTokenId().getNftTokenIdKey())
                .tokenAmount(1L)
                .bidToken(EMPTY)
                .price(EMPTY)
                .bidTokenAmount(EMPTY)
                .state(EMPTY)
                .ts(openBoxEvent.getTs())
                .build();
        operation.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        operation.setTransactionHash(transaction.getHash());
        this.save(operation);
    }

    @Override
    public List<Operation> getListById(Context context, String tokenId) {
        QueryWrapper<Operation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Operation::getChain, context.getChain());
        queryWrapper.lambda().eq(Operation::getTokenId, tokenId);

        return this.list(queryWrapper);
    }


}
