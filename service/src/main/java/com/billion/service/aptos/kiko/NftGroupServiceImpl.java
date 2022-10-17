package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.TransactionPayload;
import com.aptos.utils.Hex;
import com.aptos.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftGroupMapper;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftGroup;
import com.billion.model.entity.Pair;
import com.billion.model.entity.Token;
import com.billion.model.enums.*;
import com.billion.model.exception.BizException;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
import com.billion.service.aptos.ContextService;
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
    ImageService imageService;

    @Resource
    LanguageService languageService;

    @Resource
    HandleService handleService;

    @Resource
    TokenService tokenService;

    @Resource
    PairService pairService;

    @Override
    public Map cacheMap(Context context) {
        String key = this.cacheMapKey("ids::" + context.getChain());

        Map map = this.getRedisTemplate().opsForHash().entries(key);
        if (!map.isEmpty()) {
            return map;
        }

        QueryWrapper<NftGroup> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftGroup::getIsEnabled, Boolean.TRUE);
        List<NftGroup> list = super.list(wrapper);

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
        wrapper.lambda().eq(NftGroup::getIsEnabled, Boolean.TRUE);
        List<NftGroup> list = super.list(wrapper);

        changeLanguage(context, list);

        map = list.stream().collect(Collectors.toMap(e -> e.getMeta2() + "-" + e.getBody2(), (e) -> e));
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

    private void changeLanguage(Context context, List<NftGroup> list) {
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
            var response = AptosService.getAptosClient().requestTableCollectionData(nftGroup.getMeta2(), nftGroup.getBody2());
            if (!response.isValid()) {
                nftGroup.setTotalSupply(response.getData().getMaximum());
                nftGroup.setCurrentSupply(response.getData().getSupply());

                this.updateById(nftGroup);

                this.deleteCache(id);
            }
        }

        return nftGroup;
    }

    @Override
    public boolean initialize(Serializable id) {
        QueryWrapper<NftGroup> nftGroupQueryWrapper = new QueryWrapper<>();
        nftGroupQueryWrapper.lambda().eq(NftGroup::getId, id);
        nftGroupQueryWrapper.lambda().eq(NftGroup::getChain, Chain.APTOS.getCode());
        nftGroupQueryWrapper.lambda().eq(NftGroup::getIsEnabled, Boolean.TRUE);
        var nftGroup = super.getOneThrowEx(nftGroupQueryWrapper);
        if (TransactionStatus.STATUS_3_SUCCESS == nftGroup.getTransactionStatus_()) {
            //排查是否需要执行handleService.update
            return this.initializeMarket();
        }
        if (TransactionStatus.STATUS_1_READY != nftGroup.getTransactionStatus_()) {
            return false;
        }

        QueryWrapper<com.billion.model.entity.Language> languageQueryWrapper = new QueryWrapper<>();
        languageQueryWrapper.lambda().eq(com.billion.model.entity.Language::getLanguage, Language.EN.getCode());
        languageQueryWrapper.lambda().eq(com.billion.model.entity.Language::getKey, nftGroup.getDisplayName());
        var displayName = languageService.getOneThrowEx(languageQueryWrapper).getValue();

        languageQueryWrapper = new QueryWrapper<>();
        languageQueryWrapper.lambda().eq(com.billion.model.entity.Language::getLanguage, Language.EN.getCode());
        languageQueryWrapper.lambda().eq(com.billion.model.entity.Language::getKey, nftGroup.getDescription());
        var description = languageService.getOneThrowEx(languageQueryWrapper).getValue();

        if (StringUtils.isEmpty(displayName)
                || StringUtils.isEmpty(description)
                || StringUtils.isEmpty(nftGroup.getTotalSupply())
                || StringUtils.isEmpty(nftGroup.getOwner())
                || DEFAULT_TEXT.equals(displayName)
                || DEFAULT_TEXT.equals(description)
        ) {
            nftGroup.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            nftGroup.setTransactionHash(EMPTY);
            super.updateById(nftGroup);

            return false;
        }

        if (25 < displayName.length()) {
            nftGroup.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            nftGroup.setTransactionHash(EMPTY);
            super.updateById(nftGroup);

            throw new BizException("display name too long, max 25");
        }

        var image = this.imageService.add(nftGroup.getUri(), nftGroup.getClass().getSimpleName() + ":" + nftGroup.getId());

        TransactionPayload transactionPayload = TransactionPayload.builder()
                .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                .function("0x3::token::create_collection_script")
                .arguments(List.of(
                        Hex.encode(displayName),
                        Hex.encode(description),
                        Hex.encode(image.getProxy()),
                        nftGroup.getTotalSupply(),//TODO
                        List.of(true, true, true)//TODO
                ))
                .typeArguments(List.of())
                .build();

        var response = AptosService.getAptosClient().requestSubmitTransaction(
                ContextService.getKikoOwner(),
                transactionPayload);
        if (response.isValid()) {
            nftGroup.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            nftGroup.setTransactionHash(EMPTY);
            super.updateById(nftGroup);

            return false;
        }

        if (!AptosService.checkTransaction(response.getData().getHash())) {
            nftGroup.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            nftGroup.setTransactionHash(response.getData().getHash());

            return false;
        }

        nftGroup.setUri(image.getProxy());

        nftGroup.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        nftGroup.setTransactionHash(response.getData().getHash());

        super.updateById(nftGroup);

        //TODO leeqiang检查什么意思
        this.handleService.update(nftGroup.getOwner());

        //TODO 删除缓存

        return this.initializeMarket();
    }

    @Override
    public boolean initializeMarket() {
        Context context = Context.builder()
                .chain(Chain.APTOS.getCode())
                .language(Language.EN.getCode())
                .build();

        var function = ContextService.getKikoOwner() + "::secondary_market::nft_init";

        var askToken = Token.builder()
                .id(0L)
                .build();

        var tokens = tokenService.getByScene(context, TokenScene.MARKET.getCode());
        for (Token bidToken : tokens) {
            QueryWrapper<Pair> pairQueryWrapper = new QueryWrapper<>();
            pairQueryWrapper.lambda().eq(Pair::getContract, function);
            pairQueryWrapper.lambda().eq(Pair::getAskToken, askToken.getId());
            pairQueryWrapper.lambda().eq(Pair::getBidToken, bidToken.getId());
            var pair = pairService.getOne(pairQueryWrapper, false);
            if (Objects.isNull(pair)) {
                com.aptos.request.v1.model.Resource bidTokenResource = com.aptos.request.v1.model.Resource.builder()
                        .moduleAddress(bidToken.getModuleAddress())
                        .moduleName(bidToken.getModuleName())
                        .resourceName(bidToken.getStructName())
                        .build();

                TransactionPayload transactionPayload = TransactionPayload.builder()
                        .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                        .function(function)
                        .arguments(List.of())
                        .typeArguments(List.of(bidTokenResource.resourceTag()))
                        .build();

                var response = AptosService.getAptosClient().requestSubmitTransaction(
                        ContextService.getKikoOwner(),
                        transactionPayload);
                if (response.isValid()) {
                    return false;
                }

                if (!AptosService.checkTransaction(response.getData().getHash())) {
                    return false;
                }

                pair = Pair.builder()
                        .contract(function)
                        .askToken(askToken.getId())
                        .bidToken(bidToken.getId())
                        .build();

                pairService.save(pair);
            }
        }

        return true;
    }

}