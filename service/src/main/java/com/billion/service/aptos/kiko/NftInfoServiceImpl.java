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
import com.billion.model.exception.BizException;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Serializable;
import java.time.Duration;
import java.util.*;

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
    HandleService handleService;

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

        var handle = handleService.getByAccount(nftGroup.getOwner());
        if (StringUtils.isEmpty(handle.getCollectionsTokenDataHandle())) {
            return false;
        }

        var context = Context.builder()
                .language(Language.EN.getCode())
                .build();

        var nftGroupDisplayName = languageService.getByKey(context, nftGroup.getDisplayName());
        if (StringUtils.isEmpty(nftGroupDisplayName)
                || DEFAULT_TEXT.equals(nftGroupDisplayName)
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
                    || StringUtils.isEmpty(nftInfo.getOwner())
                    || DEFAULT_TEXT.equals(displayName)
                    || DEFAULT_TEXT.equals(description)
                    || DEFAULT_TEXT.equals(uri)
            ) {
                return;
            }

            if (26 < displayName.length()) {
                throw new BizException("display name too long, max 26");
            }

            TransactionPayload transactionPayload = TransactionPayload.builder()
                    .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                    .function("0x3::token::create_token_script")
                    .arguments(List.of(
                            Hex.encode(nftGroupDisplayName),
                            Hex.encode(displayName),
                            Hex.encode(description),
                            "1",
                            "1",
                            Hex.encode(nftInfo.getUri()),
                            nftGroup.getOwner(),
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
                    nftGroup.getOwner(),
                    transactionPayload);
            if (AptosService.checkTransaction(transaction.getHash())) {
                nftInfo.setOwner(nftGroup.getOwner());

                nftInfo.setTransactionHash(transaction.getHash());
                nftInfo.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);

                nftInfo.setTableHandle(handle.getCollectionsTokenDataHandle());
                nftInfo.setTableCollection(Hex.encode(nftGroupDisplayName));
                nftInfo.setTableCreator(nftGroup.getOwner());
                nftInfo.setTableName(Hex.encode(displayName));

                super.updateById(nftInfo);
            }
        });

        return true;
    }

//    {
//        "key_type": "0x3::token::TokenDataId",
//            "value_type": "0x3::token::TokenData",
//            "key": {
//        "creator": "0x4cd5040c25c069143f22995f0deaae6bfb674949302b008678455174b8ea8104",
//                "collection": "0xe68891e698afe5908de5ad973334",
//                "name": "0x34633361636266642d643537662d343862642d623864652d306530613730363036306131"
//    }
//    }

}