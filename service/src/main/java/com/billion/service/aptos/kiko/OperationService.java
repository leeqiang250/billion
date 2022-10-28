package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Event;
import com.aptos.request.v1.model.Transaction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.billion.model.dto.Context;
import com.billion.model.dto.OperationDto;
import com.billion.model.entity.Operation;
import com.billion.model.event.*;

import java.util.List;

/**
 * @author jason
 */
public interface OperationService extends IService<Operation> {

    Operation addBoxMakerOpt(Transaction transaction, Event event, BoxMakerEvent boxMakerEvent);

    Operation addBoxTakerOpt(Transaction transaction, Event event, BoxTakerEvent boxTakerEvent);

    Operation addBoxBidOpt(Transaction transaction, Event event, BoxBidEvent boxBidEvent);

    Operation addBoxCancelOpt(Transaction transaction, Event event, BoxCancelEvent boxCancelEvent);

    Operation addNftMintOpt(Transaction transaction, Event event, NftCreateTokenDataEvent nftCreateTokenDataEvent);

    Operation addNftWithdrawOpt(Transaction transaction, Event event, NftWithdrawEvent nftWithdrawEvent);

    Operation addNftDepositOpt(Transaction transaction, Event event, NftDepositEvent nftDepositEvent);

    Operation addNftBurnTokenOpt(Transaction transaction, Event event, NftBurnTokenEvent nftBurnTokenEvent);

    Operation addNftMakerOpt(Transaction transaction, Event event, MarketMakerEvent nftMakerEvent);

    Operation addNftTakerOpt(Transaction transaction, Event event, MarketTakerEvent nftTakerEvent);

    Operation addNftBidOpt(Transaction transaction, Event event, MarketBidEvent nftBidEvent);

    Operation addNftCancelOpt(Transaction transaction, Event event, MarketCancelEvent nftCancelEvent);

    void addOpenBoxOpt(OpenBoxEvent openBoxEvent, Transaction transaction);

    /**
     * getListById
     * @param context
     * @param tokenId
     * @return
     */
    List<OperationDto> getListById(Context context, String tokenId);

    /**
     * getSaleRecord
     * @param context
     * @param account
     * @return
     */
    List<OperationDto> getSaleRecord(Context context, String account);

    /**
     * getBuyRecord
     * @param context
     * @param account
     * @return
     */
    List<OperationDto> getBuyRecord(Context context, String account);
}
