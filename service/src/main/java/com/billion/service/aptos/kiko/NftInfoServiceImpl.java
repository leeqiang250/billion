package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Response;
import com.aptos.request.v1.model.TableTokenData;
import com.aptos.request.v1.model.TransactionPayload;
import com.aptos.utils.Hex;
import com.aptos.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftInfoMapper;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftInfo;
import com.billion.model.enums.Language;
import com.billion.model.enums.NftPropertyType;
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
import java.util.stream.Collectors;

import static com.billion.model.constant.RequestPath.DEFAULT_TEXT;
import static com.billion.model.constant.RequestPath.EMPTY;

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

    @Resource
    NftClassService nftClassService;

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

    @Override
    public boolean mint(Serializable groupId) {
        if (!this.nftGroupService.initialize()) {
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
        wrapper.lambda().eq(NftInfo::getTransactionStatus, TransactionStatus.STATUS_1_READY.getCode());
        var nftInfos = super.list(wrapper);
        for (int i = 0; i < nftInfos.size(); i++) {
            var nftInfo = nftInfos.get(i);

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
                nftInfo.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
                nftInfo.setTransactionHash(EMPTY);
                super.updateById(nftInfo);

                return false;
            }

            if (26 < displayName.length()) {
                nftInfo.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
                nftInfo.setTransactionHash(EMPTY);
                super.updateById(nftInfo);

                throw new BizException("display name too long, max 26");
            }

            Map<String, List<String>> classMap = nftClassService.getClassForMint(nftInfo.getId().toString());
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
                            classMap.get(NftPropertyType.KEYS.getType()),
                            classMap.get(NftPropertyType.VALUES.getType()),
                            classMap.get(NftPropertyType.TYPES.getType())
                    ))
                    .typeArguments(List.of())
                    .build();

            var response = AptosService.getAptosClient().requestSubmitTransaction(
                    nftGroup.getOwner(),
                    transactionPayload);
            if (response.isValid()) {
                nftInfo.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
                nftInfo.setTransactionHash(EMPTY);
                super.updateById(nftInfo);

                return false;
            }

            if (AptosService.checkTransaction(response.getData().getHash())) {
                nftInfo.setOwner(nftGroup.getOwner());

                nftInfo.setTransactionHash(response.getData().getHash());
                nftInfo.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);

                nftInfo.setTableHandle(handle.getCollectionsTokenDataHandle());
                nftInfo.setTableCollection(Hex.encode(nftGroupDisplayName));
                nftInfo.setTableCreator(nftGroup.getOwner());
                nftInfo.setTableName(Hex.encode(displayName));

                super.updateById(nftInfo);
            } else {
                nftInfo.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
                nftInfo.setTransactionHash(response.getData().getHash());
                super.updateById(nftInfo);

                return false;
            }
        }

        //TODO 删除缓存

        return true;
    }

    @Override
    public Response<TableTokenData> getTableTokenData(Serializable id) {
        var nftInfo = super.getById(id);
        if (Objects.isNull(nftInfo)) {
            return null;
        }

        var response = AptosService.getAptosClient().requestTableTokenData(
                nftInfo.getTableHandle(),
                nftInfo.getTableCreator(),
                nftInfo.getTableCollection(),
                nftInfo.getTableName()
        );

        if (!response.isValid()) {
            response.getData().decode();
        }

        return response;
    }

    @Override
    public List<NftInfo> getListByGroup(Context context, String type, String groupId) {

        QueryWrapper<NftInfo> wrapper = new QueryWrapper<>();
        if ("group".equals(type)) {
            wrapper.lambda().eq(NftInfo::getNftGroupId, groupId);
        } else if ("boxGroup".equals(type)) {
            wrapper.lambda().eq(NftInfo::getBoxGroupId, groupId);
        }
        //TODO:是否需要状态区别
//        wrapper.lambda().eq(NftInfo::getTransactionStatus, TransactionStatus.STATUS_1_READY.getCode());
        var nftInfos = super.list(wrapper);
        changeLanguage(context, nftInfos);

        return nftInfos;
    }

    private void changeLanguage(Context context, List<NftInfo> list) {
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