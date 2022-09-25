package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.TransactionPayload;
import com.aptos.utils.Hex;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftInfoMapper;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftInfo;
import com.billion.model.enums.TransactionStatus;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Serializable;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class NftInfoServiceImpl extends AbstractCacheService<NftInfoMapper, NftInfo> implements NftInfoService {

    @Resource
    NftGroupService nftGroupService;

    @Override
    public Map cacheMap(Context context) {
        return null;
    }

    @Override
    public NftInfo cacheById(Context context, Serializable id, Duration timeout) {
        String key = this.cacheByIdKey(null, id);

        Object value = this.getRedisTemplate().opsForValue().get(key);
        if (Objects.isNull(value)) {
            NftInfo e = this.getById(id);
            if (Objects.nonNull(e)) {
                value = e;

                this.getRedisTemplate().opsForValue().set(key, e, timeout);
            }
        } else {
            value = this.fromObject(value);
        }

        return (NftInfo) value;
    }

    @PostConstruct
    public void sfdfds() {
        this.mint("1");
        this.mint("1");
        this.mint("1");
        this.mint("1");
        this.mint("1");
        this.mint("1");
        this.mint("1");
    }

    public boolean mint(Serializable groupId) {
        if (!this.nftGroupService.mint(groupId)) {
            return false;
        }

        var nftGroup = nftGroupService.getById(groupId);
        if (Objects.isNull(nftGroup)) {
            return false;
        }

        QueryWrapper<NftInfo> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftInfo::getNftGroupId, groupId);

        var nftInfos = super.getBaseMapper().selectList(wrapper);
        nftInfos.forEach(nftInfo -> {
            if (TransactionStatus.STATUS_1_READY == nftInfo.getTransactionStatus_()) {
                TransactionPayload transactionPayload = TransactionPayload.builder()
                        .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                        .function("0x3::token::create_token_script")
                        .arguments(List.of(
                                Hex.encode(nftGroup.getDisplayName()),
                                Hex.encode(nftInfo.getDisplayName()),
                                Hex.encode(nftInfo.getDescription()),
                                "1",
                                "1",
                                Hex.encode(nftInfo.getUri()),
                                nftInfo.getOwner(),
                                "3",
                                "1",
                                List.of(true, true, true, true, true),
                                List.of(Hex.encode(UUID.randomUUID().toString())),
                                List.of(Hex.encode(UUID.randomUUID().toString())),
                                List.of(Hex.encode(UUID.randomUUID().toString()))
                        ))
                        .typeArguments(List.of())
                        .build();


                var transaction = AptosService.getAptosClient().requestSubmitTransaction(
                        nftInfo.getOwner(),
                        transactionPayload);
                if (AptosService.checkTransaction(transaction.getHash())) {
                    nftInfo.setTransactionHash(transaction.getHash());
                    nftInfo.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);

                    super.updateById(nftInfo);
                }
            }
        });

        return false;
    }

}