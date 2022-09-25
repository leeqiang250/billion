package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.TransactionPayload;
import com.aptos.utils.Hex;
import com.aptos.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftGroupMapper;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftGroup;
import com.billion.model.enums.CacheTsType;
import com.billion.model.enums.Chain;
import com.billion.model.enums.Language;
import com.billion.model.enums.Mint;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.billion.model.constant.RequestPath.DEFAULT_TEXT;
import static com.billion.model.constant.RequestPath.EMPTY;

/**
 * @author liqiang
 */
@Slf4j
@Service
@SuppressWarnings({"all"})
public class NftGroupServiceImpl extends AbstractCacheService<NftGroupMapper, NftGroup> implements NftGroupService {

    @Resource
    LanguageService languageService;

    @Resource
    HandleService handleService;

    @Override
    public Map cacheMap(Context context) {
        String key = this.cacheMapKey("ids::" + context.getChain());

        Map map = this.getRedisTemplate().opsForHash().entries(key);
        if (!map.isEmpty()) {
            return map;
        }

        QueryWrapper<NftGroup> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftGroup::getEnabled, Boolean.TRUE);
        List<NftGroup> list = this.getBaseMapper().selectList(wrapper);

        changeLanguage(context, list);

        map = list.stream().collect(Collectors.toMap(e -> e.getId().toString(), (e) -> e));
        this.getRedisTemplate().opsForHash().putAll(key, map);
        this.getRedisTemplate().expire(key, this.cacheSecond(CacheTsType.MIDDLE));

        return map;
    }

    @Override
    public Map getAllByMetaBody(Context context) {
        String key = this.cacheMapKey("meta-body::" + context.key());
        Map map = this.getRedisTemplate().opsForHash().entries(key);
        if (!map.isEmpty()) {
            return map;
        }

        QueryWrapper<NftGroup> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftGroup::getEnabled, Boolean.TRUE);
        List<NftGroup> list = this.getBaseMapper().selectList(wrapper);

        changeLanguage(context, list);

        map = list.stream().collect(Collectors.toMap(e -> e.getMeta() + "-" + e.getBody(), (e) -> e));
        this.getRedisTemplate().opsForHash().putAll(key, map);
        this.getRedisTemplate().expire(key, this.cacheSecond(CacheTsType.MIDDLE));

        return map;
    }

    @Override
    public NftGroup cacheById(Context context, Serializable id) {
        String key = this.cacheByIdKey(context.key(), id);

        Object value = this.getRedisTemplate().opsForHash().get(key, id);
        if (Objects.isNull(value)) {
            Boolean result = this.getRedisTemplate().hasKey(key);
            if (Objects.nonNull(result) && !result) {
                value = this.cacheMap(context).get(id);
            }
        } else {
            value = this.fromObject(value);
        }

        return (NftGroup) value;
    }

    @Override
    public NftGroup getByMetaBody(Context context, String meta, String body) {
        String id = meta + "-" + body;
        String key = this.cacheByIdKey(context.key(), id);

        Object value = this.getRedisTemplate().opsForHash().get(key, id);
        if (Objects.isNull(value)) {
            Boolean result = this.getRedisTemplate().hasKey(key);
            if (Objects.nonNull(result) && !result) {
                value = this.getAllByMetaBody(context).get(id);
            }
        } else {
            value = this.fromObject(value);
        }

        return (NftGroup) value;
    }

    public void changeLanguage(Context context, List<NftGroup> list) {
        Set setDisplayName = list.stream().map(e -> e.getDisplayName()).collect(Collectors.toSet());
        Set setDescription = list.stream().map(e -> e.getDescription()).collect(Collectors.toSet());

        Map mapDisplayName = languageService.getByKeys(context, setDisplayName);
        Map mapDescription = languageService.getByKeys(context, setDescription);

        list.forEach(e -> {
            e.setDisplayName(mapDisplayName.get(e.getDisplayName()).toString());
            e.setDescription(mapDescription.get(e.getDescription()).toString());
        });
    }

    @Override
    public NftGroup updateSupply(Serializable id) {
        NftGroup nftGroup = this.getById(id);
        if (Chain.APTOS.getCode().equals(nftGroup.getChain())) {
            //TODO
            var tableCollectionData = AptosService.getAptosClient().requestTableCollectionData(nftGroup.getMeta(), nftGroup.getBody());
            nftGroup.setTotalSupply(tableCollectionData.getMaximum());
            nftGroup.setCurrentSupply(tableCollectionData.getSupply());

            this.updateById(nftGroup);

            this.deleteCache(id);
        }

        return nftGroup;
    }

    public boolean mint(Serializable id) {
        QueryWrapper<NftGroup> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftGroup::getId, id);
        wrapper.lambda().eq(NftGroup::getInitializeHash, EMPTY);
        wrapper.lambda().eq(NftGroup::getEnabled, true);

        var nftGroup = this.getBaseMapper().selectOne(wrapper);
        if (Objects.isNull(nftGroup)
                || Mint.MINT_NOT != nftGroup.getMint_()
        ) {
            return false;
        }

        var context = Context.builder()
                .language(Language.EN.getCode())
                .build();
        var displayName = languageService.getByKey(context, nftGroup.getDisplayName());
        var description = languageService.getByKey(context, nftGroup.getDescription());
        var uri = nftGroup.getUri();

        if (StringUtils.isEmpty(displayName)
                || StringUtils.isEmpty(description)
                || StringUtils.isEmpty(uri)
                || StringUtils.isEmpty(nftGroup.getTotalSupply())
                || DEFAULT_TEXT.equals(displayName)
                || DEFAULT_TEXT.equals(description)
                || DEFAULT_TEXT.equals(uri)
        ) {
            return false;
        }

        TransactionPayload transactionPayload = TransactionPayload.builder()
                .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                .function("0x3::token::create_collection_script")
                .arguments(List.of(
                        Hex.encode(displayName),
                        Hex.encode(description),
                        Hex.encode(uri),
                        nftGroup.getTotalSupply(),//TODO
                        List.of(true, true, true)//TODO
                ))
                .typeArguments(List.of())
                .build();

        var transaction = AptosService.getAptosClient().requestSubmitTransaction(
                nftGroup.getOwner(),
                transactionPayload);
        if (AptosService.checkTransaction(transaction.getHash())) {
            nftGroup.setInitializeHash(transaction.getHash());
            nftGroup.setMint_(Mint.MINT_SUCCESS);

            this.updateById(nftGroup);

            this.handleService.update(nftGroup.getOwner());
        }

        return false;
    }

}