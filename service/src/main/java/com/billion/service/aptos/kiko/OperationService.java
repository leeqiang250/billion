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

    Operation addMarketBoxMakerOpt(Transaction transaction, Event event, MarketBoxMakerEvent boxMakerEvent);

    Operation addMarketBoxTakerOpt(Transaction transaction, Event event, MarketBoxTakerEvent boxTakerEvent);

    Operation addMarketBoxBidOpt(Transaction transaction, Event event, MarketBoxBidEvent boxBidEvent);

    Operation addMarketBoxCancelOpt(Transaction transaction, Event event, MarketBoxCancelEvent boxCancelEvent);

    Operation addNftMintOpt(Transaction transaction, Event event, NftCreateTokenDataEvent nftCreateTokenDataEvent);

    Operation addNftWithdrawOpt(Transaction transaction, Event event, NftWithdrawEvent nftWithdrawEvent);

    Operation addNftDepositOpt(Transaction transaction, Event event, NftDepositEvent nftDepositEvent);

    Operation addNftBurnTokenOpt(Transaction transaction, Event event, NftBurnTokenEvent nftBurnTokenEvent);

    Operation addMarketNftMakerOpt(Transaction transaction, Event event, MarketNftMakerEvent nftMakerEvent);

    Operation addMarketNftTakerOpt(Transaction transaction, Event event, MarketNftTakerEvent nftTakerEvent);

    Operation addMarketNftBidOpt(Transaction transaction, Event event, MarketNftBidEvent nftBidEvent);

    Operation addMarketNftCancelOpt(Transaction transaction, Event event, MarketNftCancelEvent nftCancelEvent);

    void addOpenBoxOpt(OpenBoxEvent openBoxEvent, Transaction transaction);

    /**
     * getListById
     *
     * @param context
     * @param tokenId
     * @return
     */
    List<OperationDto> getListById(Context context, String tokenId);

    /**
     * getSaleRecord
     *
     * @param context
     * @param account
     * @return
     */
    List<OperationDto> getSaleRecord(Context context, String account);

    /**
     * getBuyRecord
     *
     * @param context
     * @param account
     * @return
     */
    List<OperationDto> getBuyRecord(Context context, String account);
}
