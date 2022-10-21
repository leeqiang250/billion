package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Event;
import com.aptos.request.v1.model.TokenDataId;
import com.aptos.request.v1.model.TokenId;
import com.aptos.request.v1.model.Transaction;
import com.aptos.utils.Hex;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftMapper;
import com.billion.model.dto.Context;
import com.billion.model.entity.Nft;
import com.billion.model.entity.Operation;
import com.billion.model.enums.Chain;
import com.billion.model.enums.OperationType;
import com.billion.model.enums.TransactionStatus;
import com.billion.model.event.NftCreateTokenDataEvent;
import com.billion.model.event.NftDepositEvent;
import com.billion.model.event.NftWithdrawEvent;
import com.billion.model.event.OpenBoxEvent;
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
        Operation operation = Operation.builder()
                .chain(Chain.APTOS.getCode())
                .owner(event.getGuid().getAccountAddress())
                .type(OperationType.NFT_MINT_EVENT.getType())
                .tokenId(nftCreateTokenDataEvent.getTokenId().getNftTokenIdKey())
                .tokenAmount(1L)
                .build();
        operation.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        operation.setTransactionHash(transaction.getHash());
        operationService.save(operation);

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
        Operation operation = Operation.builder()
                .chain(Chain.APTOS.getCode())
                .owner(event.getGuid().getAccountAddress())
                .type(OperationType.NFT_WITHDRAW_EVENT.getType())
                .tokenId(nftWithdrawEvent.getId().getNftTokenIdKey())
                .tokenAmount(1L)
                .build();
        operation.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        operation.setTransactionHash(transaction.getHash());
        operationService.save(operation);

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
        Operation operation = Operation.builder()
                .chain(Chain.APTOS.getCode())
                .owner(event.getGuid().getAccountAddress())
                .type(OperationType.NFT_DEPOSIT_EVENT.getType())
                .tokenId(nftDepositEvent.getId().getNftTokenIdKey())
                .tokenAmount(1L)
                .build();
        operation.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        operation.setTransactionHash(transaction.getHash());
        operationService.save(operation);

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

}