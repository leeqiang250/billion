package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Response;
import com.aptos.request.v1.model.TableTokenData;
import com.aptos.request.v1.model.TransactionPayload;
import com.aptos.utils.Hex;
import com.aptos.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftMetaMapper;
import com.billion.model.dto.Context;
import com.billion.model.entity.BoxGroup;
import com.billion.model.entity.NftGroup;
import com.billion.model.entity.NftMeta;
import com.billion.model.entity.Token;
import com.billion.model.enums.Chain;
import com.billion.model.enums.Language;
import com.billion.model.enums.TransactionStatus;
import com.billion.model.exception.BizException;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
import com.billion.service.aptos.ContextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Serializable;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static com.billion.model.constant.RequestPath.DEFAULT_TEXT;
import static com.billion.model.constant.RequestPath.EMPTY;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class NftMetaServiceImpl extends AbstractCacheService<NftMetaMapper, NftMeta> implements NftMetaService {

    @Resource
    LanguageService languageService;

    @Resource
    HandleService handleService;

    @Resource
    NftGroupService nftGroupService;

    @Resource
    BoxGroupService boxGroupService;

    @Resource
    TokenService tokenService;

    @Resource
    NftClassService nftClassService;

    @Override
    public Map cacheMap(Context context) {
        return null;
    }

    @Override
    public NftMeta cacheById(Context context, Serializable id, Duration timeout) {
        String key = this.cacheByIdKey(null, id);

        Object value = this.getRedisTemplate().opsForValue().get(key);
        if (Objects.isNull(value)) {
            NftMeta e = this.getById(id);
            if (Objects.nonNull(e)) {
                value = e;

                this.getRedisTemplate().opsForValue().set(key, e, timeout);
            }
        } else {
            value = this.fromObject(value);
        }

        return (NftMeta) value;
    }

    @Override
    public boolean initialize(Serializable boxGroupId, Serializable nftGroupId) {
        QueryWrapper<BoxGroup> boxGroupQueryWrapper = new QueryWrapper<>();
        boxGroupQueryWrapper.lambda().eq(BoxGroup::getId, boxGroupId);
        boxGroupQueryWrapper.lambda().eq(BoxGroup::getChain, Chain.APTOS.getCode());
        boxGroupQueryWrapper.lambda().eq(BoxGroup::getIsEnabled, Boolean.TRUE);
        //boxGroupQueryWrapper.lambda().eq(BoxGroup::getTransactionStatus, TransactionStatus.STATUS_3_SUCCESS.getCode());
        var boxGroup = this.boxGroupService.getOneThrowEx(boxGroupQueryWrapper);

        QueryWrapper<Token> tokenQueryWrapper = new QueryWrapper<>();
        tokenQueryWrapper.lambda().eq(Token::getId, boxGroup.getAskToken());
        tokenQueryWrapper.lambda().eq(Token::getChain, Chain.APTOS.getCode());
        tokenQueryWrapper.lambda().eq(Token::getTransactionStatus, TransactionStatus.STATUS_3_SUCCESS.getCode());
        var box = this.tokenService.getOneThrowEx(tokenQueryWrapper);

        com.aptos.request.v1.model.Resource boxResource = com.aptos.request.v1.model.Resource.builder()
                .moduleAddress(box.getModuleAddress())
                .moduleName(box.getModuleName())
                .resourceName(box.getStructName())
                .build();

        QueryWrapper<NftGroup> nftGroupQueryWrapper = new QueryWrapper<>();
        nftGroupQueryWrapper.lambda().eq(NftGroup::getId, nftGroupId);
        nftGroupQueryWrapper.lambda().eq(NftGroup::getChain, Chain.APTOS.getCode());
        nftGroupQueryWrapper.lambda().eq(NftGroup::getIsEnabled, Boolean.TRUE);
        nftGroupQueryWrapper.lambda().eq(NftGroup::getTransactionStatus, TransactionStatus.STATUS_3_SUCCESS.getCode());
        var nftGroup = this.nftGroupService.getOneThrowEx(nftGroupQueryWrapper);

        var handle = handleService.getByAccount(nftGroup.getOwner());
        if (StringUtils.isEmpty(handle.getCollectionsTokenDataHandle())) {
            return false;
        }

        QueryWrapper<com.billion.model.entity.Language> languageQueryWrapper = new QueryWrapper<>();
        languageQueryWrapper.lambda().eq(com.billion.model.entity.Language::getLanguage, Language.EN.getCode());
        languageQueryWrapper.lambda().eq(com.billion.model.entity.Language::getKey, nftGroup.getDisplayName());
        var nftGroupDisplayName = languageService.getOneThrowEx(languageQueryWrapper).getValue();
        if (StringUtils.isEmpty(nftGroupDisplayName)
                || DEFAULT_TEXT.equals(nftGroupDisplayName)
        ) {
            return false;
        }


        QueryWrapper<NftMeta> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftMeta::getNftGroupId, nftGroup.getId());
        wrapper.lambda().eq(NftMeta::getIsBorn, Boolean.FALSE);
        wrapper.lambda().eq(NftMeta::getTransactionStatus, TransactionStatus.STATUS_1_READY.getCode());

        var nftMetas = super.list(wrapper);
        for (int i = 0; i < nftMetas.size(); i++) {
            var nftMeta = nftMetas.get(i);

            languageQueryWrapper = new QueryWrapper<>();
            languageQueryWrapper.lambda().eq(com.billion.model.entity.Language::getLanguage, Language.EN.getCode());
            languageQueryWrapper.lambda().eq(com.billion.model.entity.Language::getKey, nftMeta.getDisplayName());
            var displayName = languageService.getOneThrowEx(languageQueryWrapper).getValue();

            languageQueryWrapper = new QueryWrapper<>();
            languageQueryWrapper.lambda().eq(com.billion.model.entity.Language::getLanguage, Language.EN.getCode());
            languageQueryWrapper.lambda().eq(com.billion.model.entity.Language::getKey, nftMeta.getDescription());
            var description = languageService.getOneThrowEx(languageQueryWrapper).getValue();
            var uri = nftMeta.getUri();

            if (StringUtils.isEmpty(displayName)
                    || StringUtils.isEmpty(description)
                    || StringUtils.isEmpty(uri)
                    || StringUtils.isEmpty(nftMeta.getOwner())
                    || DEFAULT_TEXT.equals(displayName)
                    || DEFAULT_TEXT.equals(description)
                    || DEFAULT_TEXT.equals(uri)
            ) {
                nftMeta.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
                nftMeta.setTransactionHash(EMPTY);
                super.updateById(nftMeta);

                return false;
            }

            if (26 < displayName.length()) {
                nftMeta.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
                nftMeta.setTransactionHash(EMPTY);
                super.updateById(nftMeta);

                throw new BizException("display name too long, max 26");
            }

            Map<String, List<String>> classMap = nftClassService.getClassForMint(nftMeta.getId().toString());
            TransactionPayload transactionPayload = TransactionPayload.builder()
                    .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                    .function(ContextService.getKikoOwner() + "help::mint_token_with_box")
                    .arguments(List.of(
                            Hex.encode(nftGroupDisplayName),
                            Hex.encode(displayName),
                            Hex.encode(description),
                            "1",
                            "1",
                            Hex.encode(nftMeta.getUri()),
                            nftGroup.getOwner(),
                            "3",
                            "1",
                            List.of(true, true, true, true, true),
                            List.of(),
                            List.of(),
                            List.of()
                            //TODO key不能重复
                            //classMap.get(NftPropertyType.KEYS.getType()),
                            //classMap.get(NftPropertyType.VALUES.getType()),
                            //classMap.get(NftPropertyType.TYPES.getType())
                    ))
                    .typeArguments(List.of(boxResource.resourceTag()))
                    .build();

            var response = AptosService.getAptosClient().requestSubmitTransaction(
                    nftGroup.getOwner(),
                    transactionPayload);
            if (response.isValid()) {
                nftMeta.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
                nftMeta.setTransactionHash(EMPTY);
                super.updateById(nftMeta);

                return false;
            }

            if (!AptosService.checkTransaction(response.getData().getHash())) {
                nftMeta.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
                nftMeta.setTransactionHash(response.getData().getHash());
                super.updateById(nftMeta);

                return false;
            }

            nftMeta.setOwner(nftGroup.getOwner());

            nftMeta.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
            nftMeta.setTransactionHash(response.getData().getHash());

            nftMeta.setTableHandle(handle.getCollectionsTokenDataHandle());
            nftMeta.setTableCollection(Hex.encode(nftGroupDisplayName));
            nftMeta.setTableCreator(nftGroup.getOwner());
            nftMeta.setTableName(Hex.encode(displayName));

            super.updateById(nftMeta);
        }

        //TODO 删除缓存

        return true;
    }

    @Override
    public Response<TableTokenData> getTableTokenData(Serializable id) {
        var nftMeta = super.getById(id);
        if (Objects.isNull(nftMeta)) {
            return null;
        }

        var response = AptosService.getAptosClient().requestTableTokenData(
                nftMeta.getTableHandle(),
                nftMeta.getTableCreator(),
                nftMeta.getTableCollection(),
                nftMeta.getTableName()
        );

        if (!response.isValid()) {
            response.getData().decode();
        }

        return response;
    }

    @Override
    public List<NftMeta> getListByGroup(Context context, String type, String groupId) {
        QueryWrapper<NftMeta> wrapper = new QueryWrapper<>();
        if ("group".equals(type)) {
            wrapper.lambda().eq(NftMeta::getNftGroupId, groupId);
        } else if ("boxGroup".equals(type)) {
            wrapper.lambda().eq(NftMeta::getBoxGroupId, groupId);
        }
        //TODO:是否需要状态区别
//        wrapper.lambda().eq(nftMeta::getTransactionStatus, TransactionStatus.STATUS_1_READY.getCode());
        var nftMetas = super.list(wrapper);
        changeLanguage(context, nftMetas);

        return nftMetas;
    }

    private void changeLanguage(Context context, List<NftMeta> list) {
        Set setDisplayName = list.stream().map(e -> e.getDisplayName()).collect(Collectors.toSet());
        Set setDescription = list.stream().map(e -> e.getDescription()).collect(Collectors.toSet());

        Map mapDisplayName = languageService.getByKeys(context, setDisplayName);
        Map mapDescription = languageService.getByKeys(context, setDescription);

        list.forEach(e -> {
            e.setDisplayName(mapDisplayName.get(e.getDisplayName()).toString());
            e.setDescription(mapDescription.get(e.getDescription()).toString());
        });
    }
}