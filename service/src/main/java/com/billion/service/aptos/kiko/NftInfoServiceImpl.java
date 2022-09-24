package com.billion.service.aptos.kiko;

import com.alibaba.fastjson2.JSONObject;
import com.aptos.request.v1.model.TransactionPayload;
import com.aptos.utils.Hex;
import com.billion.dao.aptos.kiko.NftInfoMapper;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftGroup;
import com.billion.model.entity.NftInfo;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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

    /**
     * updateState
     *
     * @param id
     * @param state
     * @return
     */
    @Override
    public NftInfo updateState(String id, Integer state) {
        NftInfo nftInfo = this.getById(id);
        if (!Objects.isNull(nftInfo)) {
            nftInfo.setState(state);

            this.updateById(nftInfo);

            this.deleteCache(this.cacheByIdKey(null, id));
        }
        return nftInfo;
    }

    @PostConstruct
    public void sfdfds() {
        this.qewe();
        this.qewe();
        this.qewe();
        this.qewe();
    }

    public void qewe() {
        //                .function("0x3::token::create_collection_script")
        String ss = UUID.randomUUID().toString();
        System.out.println("aa:" + ss);
        TransactionPayload transactionPayload = TransactionPayload.builder()
                .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                .function("0x3::token::create_token_script")
                .arguments(List.of(
                        Hex.encode("我是名字34"),
                        Hex.encode(ss),
                        Hex.encode(ss),
                        "1",
                        "1",
                        Hex.encode(ss),
                        "0x4cd5040c25c069143f22995f0deaae6bfb674949302b008678455174b8ea8104",
                        "888",
                        "999",
                        List.of(true, true, true, true, true),
                        List.of(Hex.encode(ss)),
                        List.of(Hex.encode(ss)),
                        List.of(Hex.encode(ss))
                ))
                .typeArguments(List.of())
                .build();

        var transaction = AptosService.getAptosClient().requestSubmitTransaction(
                "0x4cd5040c25c069143f22995f0deaae6bfb674949302b008678455174b8ea8104",
                transactionPayload);
        if (AptosService.checkTransaction(transaction.getHash())) {


        }


//        public entry fun create_token_script(
//                account: &signer,
//                collection: String,
//                name: String,
//                description: String,
//                balance: u64,
//                maximum: u64,
//                uri: String,
//                royalty_payee_address: address,
//                royalty_points_denominator: u64,
//                royalty_points_numerator: u64,
//                mutate_setting: vector<bool>,
//        property_keys: vector<String>,
//        property_values: vector<vector<u8>>,
//        property_types: vector<String>
//    ) acquires Collections, TokenStore {

    }

}