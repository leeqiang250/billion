package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Event;
import com.aptos.request.v1.model.Transaction;
import com.billion.model.dto.Context;
import com.billion.model.entity.Nft;
import com.billion.model.event.NftCreateTokenDataEvent;
import com.billion.model.event.NftDepositEvent;
import com.billion.model.event.NftWithdrawEvent;
import com.billion.model.service.ICacheService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author liqiang
 */
public interface NftService extends ICacheService<Nft> {

    /**
     * removeGe
     *
     * @param version version
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    boolean removeGe(Long version);

    /**
     * isNftCreateTokenDataEvent
     *
     * @param event event
     * @return boolean
     */
    boolean isNftCreateTokenDataEvent(Event event);

    /**
     * isWithdrawEvent
     *
     * @param event event
     * @return boolean
     */
    boolean isNftWithdrawEvent(Event event);

    /**
     * isDepositEvent
     *
     * @param event event
     * @return boolean
     */
    boolean isNftDepositEvent(Event event);

    /**
     * isOpenBoxEvent
     *
     * @param event event
     * @return boolean
     */
    boolean isOpenBoxEvent(Event event);

    /**
     * addNftCreateTokenDataEvent
     *
     * @param transaction             transaction
     * @param event                   event
     * @param nftCreateTokenDataEvent nftCreateTokenDataEvent
     * @return Nft
     */
    @Transactional(rollbackFor = Exception.class)
    Nft addNftCreateTokenDataEvent(Transaction transaction, Event event, NftCreateTokenDataEvent nftCreateTokenDataEvent);

    /**
     * addNftWithdrawEvent
     *
     * @param transaction      transaction
     * @param event            event
     * @param nftWithdrawEvent nftWithdrawEvent
     * @return Nft
     */
    @Transactional(rollbackFor = Exception.class)
    Nft addNftWithdrawEvent(Transaction transaction, Event event, NftWithdrawEvent nftWithdrawEvent);

    /**
     * addNftDepositEvent
     *
     * @param transaction     transaction
     * @param event           event
     * @param nftDepositEvent nftDepositEvent
     * @return Nft
     */
    @Transactional(rollbackFor = Exception.class)
    Nft addNftDepositEvent(Transaction transaction, Event event, NftDepositEvent nftDepositEvent);

    /**
     * getTokenIdsByAccount 根据地址查询当前拥有的NFT列表
     *
     * @param context
     * @param account
     * @return
     */
    List<Nft> getListByAccount(Context context, String account);
}