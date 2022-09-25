package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.TransactionPayload;
import com.aptos.utils.Hex;
import com.aptos.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftInfoMapper;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftInfo;
import com.billion.model.enums.Language;
import com.billion.model.enums.TransactionStatus;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Serializable;
import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;

import static com.billion.model.constant.RequestPath.DEFAULT_TEXT;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class NftInfoServiceImpl extends AbstractCacheService<NftInfoMapper, NftInfo> implements NftInfoService {

    @Resource
    LanguageService languageService;

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

        var context = Context.builder()
                .language(Language.EN.getCode())
                .build();

        var nftGeoupDisplayName = languageService.getByKey(context, nftGroup.getDisplayName());
        if (StringUtils.isEmpty(nftGeoupDisplayName)
                || DEFAULT_TEXT.equals(nftGeoupDisplayName)
        ) {
            return false;
        }

        QueryWrapper<NftInfo> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftInfo::getNftGroupId, groupId);
        var nftInfos = super.getBaseMapper().selectList(wrapper);

        nftInfos.forEach(nftInfo -> {
            if (TransactionStatus.STATUS_1_READY != nftInfo.getTransactionStatus_()) {
                return;
            }

            var displayName = languageService.getByKey(context, nftInfo.getDisplayName());
            var description = languageService.getByKey(context, nftInfo.getDescription());
            var uri = nftInfo.getUri();

            if (StringUtils.isEmpty(displayName)
                    || StringUtils.isEmpty(description)
                    || StringUtils.isEmpty(uri)
                    || DEFAULT_TEXT.equals(displayName)
                    || DEFAULT_TEXT.equals(description)
                    || DEFAULT_TEXT.equals(uri)
            ) {
                return;
            }

            TransactionPayload transactionPayload = TransactionPayload.builder()
                    .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                    .function("0x3::token::create_token_script")
                    .arguments(List.of(
                            Hex.encode(nftGeoupDisplayName),
                            Hex.encode(displayName),
                            Hex.encode(description),
                            "1",
                            "1",
                            Hex.encode(nftInfo.getUri()),
                            nftInfo.getOwner(),
                            "3",
                            "1",
                            List.of(true, true, true, true, true),
                            List.of(Hex.encode(UUID.randomUUID().toString()), Hex.encode(UUID.randomUUID().toString()), Hex.encode(UUID.randomUUID().toString()), Hex.encode(UUID.randomUUID().toString())),
                            List.of(Hex.encode(UUID.randomUUID().toString()), Hex.encode(UUID.randomUUID().toString()), Hex.encode(UUID.randomUUID().toString()), Hex.encode(UUID.randomUUID().toString())),
                            List.of(Hex.encode(UUID.randomUUID().toString()), Hex.encode(UUID.randomUUID().toString()), Hex.encode(UUID.randomUUID().toString()), Hex.encode(UUID.randomUUID().toString()))
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
        });

        return false;
    }

}