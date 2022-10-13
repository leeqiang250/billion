package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Event;
import com.aptos.request.v1.model.Transaction;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftMapper;
import com.billion.model.entity.Nft;
import com.billion.model.enums.Chain;
import com.billion.model.enums.TransactionStatus;
import com.billion.model.event.NftDepositEvent;
import com.billion.model.event.NftWithdrawEvent;
import com.billion.service.aptos.AbstractCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class NftServiceImpl extends AbstractCacheService<NftMapper, Nft> implements NftService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeGe(Long version) {
        QueryWrapper<Nft> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Nft::getChain, Chain.APTOS.getCode());
        queryWrapper.lambda().ge(Nft::getVersion, version);
        return super.remove(queryWrapper);
    }

    @Override
    public boolean isNftWithdrawEvent(Event event) {
        return NftWithdrawEvent.EVENT_NAME.equals(event.getType());
    }

    @Override
    public boolean isNftDepositEvent(Event event) {
        return NftDepositEvent.EVENT_NAME.equals(event.getType());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Nft addNftWithdrawEvent(Transaction transaction, Event event, NftWithdrawEvent nftWithdrawEvent) {
        Nft nft = Nft.builder()
                .chain(Chain.APTOS.getCode())
                .version(Long.parseLong(transaction.getVersion()))
                .event(event.getType())
                .owner(event.getGuid().getAccountAddress())
                .tokenId(nftWithdrawEvent.getId().getNftTokenIdKey())
                .ts(transaction.getTimestampMillisecond())
                .transactionHash(transaction.getHash())
                .isEnabled(Boolean.TRUE)
                .build();

        nft.setTransactionStatus_(TransactionStatus.STATUS_2_ING);

        super.save(nft);

        return nft;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Nft addNftDepositEvent(Transaction transaction, Event event, NftDepositEvent nftDepositEvent) {
        Nft nft = Nft.builder()
                .chain(Chain.APTOS.getCode())
                .version(Long.parseLong(transaction.getVersion()))
                .event(event.getType())
                .owner(event.getGuid().getAccountAddress())
                .tokenId(nftDepositEvent.getId().getNftTokenIdKey())
                .ts(transaction.getTimestampMillisecond())
                .transactionHash(transaction.getHash())
                .isEnabled(Boolean.TRUE)
                .build();

        nft.setTransactionStatus_(TransactionStatus.STATUS_2_ING);

        super.save(nft);

        return nft;
    }

}