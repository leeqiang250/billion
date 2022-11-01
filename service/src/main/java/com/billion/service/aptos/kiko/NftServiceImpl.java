package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Event;
import com.aptos.request.v1.model.Transaction;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftMapper;
import com.billion.model.dto.Context;
import com.billion.model.entity.Nft;
import com.billion.model.enums.Chain;
import com.billion.model.enums.TransactionStatus;
import com.billion.model.event.*;
import com.billion.service.aptos.AbstractCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class NftServiceImpl extends AbstractCacheService<NftMapper, Nft> implements NftService {

    @Resource
    OperationService operationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeGe(Long version) {
        QueryWrapper<Nft> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Nft::getChain, Chain.APTOS.getCode());
        queryWrapper.lambda().ge(Nft::getVersion, version);
        return super.remove(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Nft addNftCreateTokenDataEvent(Transaction transaction, Event event, NftCreateTokenDataEvent nftCreateTokenDataEvent) {
        Nft nft = Nft.builder()
                .chain(Chain.APTOS.getCode())
                .version(Long.parseLong(transaction.getVersion()))
                .event(event.getType())
                .owner(event.getGuid().getAccountAddress())
                .tokenId(nftCreateTokenDataEvent.getTokenId().getNftTokenIdKey())
                .ts(transaction.getTimestampMillisecond())
                .isEnabled(Boolean.TRUE)
                .build();
        nft.setTransactionStatus_(TransactionStatus.STATUS_2_ING);
        nft.setTransactionHash(transaction.getHash());

        super.save(nft);

        //铸造NFT记录
        operationService.addNftMintOpt(transaction, event, nftCreateTokenDataEvent);

        return nft;
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
                .isEnabled(Boolean.TRUE)
                .build();
        nft.setTransactionStatus_(TransactionStatus.STATUS_2_ING);
        nft.setTransactionHash(transaction.getHash());

        super.save(nft);

        //NFT转出记录
        operationService.addNftWithdrawOpt(transaction, event, nftWithdrawEvent);

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
                .isEnabled(Boolean.TRUE)
                .build();
        nft.setTransactionStatus_(TransactionStatus.STATUS_2_ING);
        nft.setTransactionHash(transaction.getHash());

        super.save(nft);

        //NFT转入记录
        operationService.addNftDepositOpt(transaction, event, nftDepositEvent);

        return nft;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Nft addNftBurnTokenEvent(Transaction transaction, Event event, NftBurnTokenEvent nftBurnTokenEvent) {
        Nft nft = Nft.builder()
                .chain(Chain.APTOS.getCode())
                .version(Long.parseLong(transaction.getVersion()))
                .event(event.getType())
                .owner(event.getGuid().getAccountAddress())
                .tokenId(nftBurnTokenEvent.getId().getNftTokenIdKey())
                .ts(transaction.getTimestampMillisecond())
                .isEnabled(Boolean.TRUE)
                .build();
        nft.setTransactionStatus_(TransactionStatus.STATUS_2_ING);
        nft.setTransactionHash(transaction.getHash());

        super.save(nft);

        //NFT销毁记录
        operationService.addNftBurnTokenOpt(transaction, event, nftBurnTokenEvent);

        return nft;
    }

    @Override
    public List<Nft> getListByAccount(Context context, String account) {
        QueryWrapper<Nft> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Nft::getChain, context.getChain());
        queryWrapper.lambda().eq(Nft::getOwner, account);

        queryWrapper.lambda().eq(Nft::getIsEnabled, Boolean.TRUE);
        queryWrapper.lambda().eq(Nft::getTransactionStatus, TransactionStatus.STATUS_2_ING.getCode());
        queryWrapper.lambda().orderByAsc(Nft::getId);
        var nftList = this.list(queryWrapper);

        Map<String, Nft> map = new HashMap<>(nftList.size());
        nftList.forEach(n -> {
            if (NftDepositEvent.EVENT_NAME.equals(n.getEvent())) {
                map.put(n.getTokenId(), n);
            }
            if (NftWithdrawEvent.EVENT_NAME.equals(n.getEvent())) {
                map.remove(n.getTokenId());
            }

        });
        return map.values().stream().collect(Collectors.toList());
    }

    @Override
    public String getOwnerByTokenId(Context context, String tokenId) {
        QueryWrapper<Nft> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Nft::getChain, context.getChain());
        queryWrapper.lambda().eq(Nft::getTokenId, tokenId);

        queryWrapper.lambda().eq(Nft::getIsEnabled, Boolean.TRUE);
        queryWrapper.lambda().eq(Nft::getTransactionStatus, TransactionStatus.STATUS_2_ING.getCode());
        queryWrapper.lambda().orderByAsc(Nft::getId);

        String owner = "";
        var nftList = this.list(queryWrapper);
        for (int i = nftList.size() - 1; i >= 0; i--) {
            if (NftDepositEvent.EVENT_NAME.equals(nftList.get(i).getEvent())) {
                owner = nftList.get(i).getOwner();
                break;
            }

        }
        return owner;
    }

}