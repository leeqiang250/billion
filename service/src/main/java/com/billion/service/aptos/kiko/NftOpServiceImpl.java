package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.TokenDataId;
import com.aptos.request.v1.model.TokenId;
import com.aptos.request.v1.model.Transaction;
import com.aptos.request.v1.model.TransactionPayload;
import com.aptos.request.v1.rpc.body.TableBody;
import com.aptos.utils.Hex;
import com.aptos.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.*;
import com.billion.model.entity.*;
import com.billion.model.enums.Chain;
import com.billion.model.enums.TransactionStatus;
import com.billion.model.event.NftComposeEvent;
import com.billion.model.event.NftSplitEvent;
import com.billion.model.resource.OpNftData;
import com.billion.service.aptos.AptosService;
import com.billion.service.aptos.ContextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.billion.model.constant.RequestPath.DEFAULT_TEXT;
import static com.billion.model.constant.RequestPath.EMPTY;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class NftOpServiceImpl implements NftOpService {

    @Resource
    ImageService imageService;

    @Resource
    NftGroupService nftGroupService;

    @Resource
    LanguageService languageService;

    @Resource
    NftSplitMapper nftSplitMapper;

    @Resource
    NftComposeMapper nftComposeMapper;

    @Resource
    NftMetaService nftMetaService;

    @Resource
    NftAttributeMetaService nftAttributeMetaService;

    @Resource
    NftAttributeTypeService nftAttributeTypeService;

    @Resource
    NftAttributeValueService nftAttributeValueService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addNftSplitEvent(Transaction transaction, NftSplitEvent nftSplitEvent) {
        if (!nftSplitEvent.isExecute()) {
            Set<String> metaIds = new HashSet<>(nftSplitEvent.getProperty().getMap().getData().size());

            var collection = Hex.decodeToString(nftSplitEvent.getCollection().getCollection());

            QueryWrapper<Language> languageQueryWrapper = new QueryWrapper<>();
            languageQueryWrapper.lambda().eq(Language::getValue, collection);
            languageQueryWrapper.lambda().eq(Language::getLanguage, com.billion.model.enums.Language.EN.getCode());
            var language = this.languageService.getOneThrowEx(languageQueryWrapper);

            QueryWrapper<NftGroup> nftGroupQueryWrapper = new QueryWrapper<>();
            nftGroupQueryWrapper.lambda().eq(NftGroup::getDisplayName, language.getKey());
            nftGroupQueryWrapper.lambda().eq(NftGroup::getChain, Chain.APTOS.getCode());
            var nftGroup = this.nftGroupService.getOneThrowEx(nftGroupQueryWrapper);

            for (var data : nftSplitEvent.getProperty().getMap().getData()) {
                if (data.getKey().startsWith("0x")) {
                    var nftMeta = NftMeta.builder()
                            .nftGroupId(nftGroup.getId())
                            .displayName(UUID.randomUUID().toString())
                            .description(UUID.randomUUID().toString())
                            .uri("https://imagedelivery.net/3mRLd_IbBrrQFSP57PNsVw/4031cc60-3e88-4f78-b412-5006ecf5c100/public")
                            .rank(0L)
                            .isBorn(Boolean.FALSE)
                            .score(EMPTY)
                            .attributeType(0)
                            .tableHandle(EMPTY)
                            .tableCollection(EMPTY)
                            .tableCreator(EMPTY)
                            .tableName(EMPTY)
                            .build();
                    nftMeta.setTransactionStatus_(TransactionStatus.STATUS_1_READY);
                    nftMeta.setTransactionHash(EMPTY);

                    QueryWrapper<NftMeta> nftMetaQueryWrapper = new QueryWrapper<>();
                    nftMetaQueryWrapper.lambda().eq(NftMeta::getNftGroupId, nftGroup.getId());
                    var list = this.nftMetaService.list(nftMetaQueryWrapper);

                    languageQueryWrapper = new QueryWrapper<>();
                    languageQueryWrapper.lambda().in(Language::getKey, list.stream().map(e -> e.getDisplayName()).collect(Collectors.toSet()));
                    languageQueryWrapper.lambda().eq(Language::getLanguage, com.billion.model.enums.Language.EN.getCode());
                    var languages = this.languageService.list(languageQueryWrapper);

                    long maxNumber = 0L;
                    for (var language_ : languages) {
                        var index = language_.getValue().lastIndexOf("#");
                        if (0 <= index) {
                            var number = language_.getValue().substring(index + 1).trim();
                            maxNumber = Math.max(maxNumber, Long.parseLong(number));
                        }
                    }
                    maxNumber++;

                    var languageDisplayName = Language.builder()
                            .language(com.billion.model.enums.Language.EN.getCode())
                            .key(nftMeta.getDisplayName())
                            .value(language.getValue() + " # " + maxNumber)
                            .build();
                    this.languageService.save(languageDisplayName);

                    this.languageService.save(Language.builder()
                            .language(com.billion.model.enums.Language.EN.getCode())
                            .key(nftMeta.getDescription())
                            .value(languageDisplayName.getValue())
                            .build());

                    var tokenId = TokenId.builder()
                            .tokenDataId(TokenDataId.builder()
                                    .creator(nftSplitEvent.getCollection().getCreator())
                                    .collection(nftSplitEvent.getCollection().getCollection())
                                    .name(Hex.encode(languageDisplayName.getValue()))
                                    .build())
                            .propertyVersion("0")
                            .build();
                    nftMeta.setTokenId(tokenId.getNftTokenIdKey());
                    this.nftMetaService.save(nftMeta);

                    metaIds.add(nftMeta.getId().toString());

                    QueryWrapper<NftAttributeType> nftAttributeTypeQueryWrapper = new QueryWrapper<>();
                    nftAttributeTypeQueryWrapper.lambda().eq(NftAttributeType::getNftGroupId, nftGroup.getId());
                    nftAttributeTypeQueryWrapper.lambda().eq(NftAttributeType::getClassName, Hex.decodeToString(data.getValue().getType()));
                    var nftAttributeType = this.nftAttributeTypeService.getOneThrowEx(nftAttributeTypeQueryWrapper);

                    QueryWrapper<NftAttributeMeta> nftAttributeMetaQueryWrapper = new QueryWrapper<>();
                    nftAttributeMetaQueryWrapper.lambda().eq(NftAttributeMeta::getNftAttributeTypeId, nftAttributeType.getId());
                    nftAttributeMetaQueryWrapper.lambda().eq(NftAttributeMeta::getAttribute, Hex.decodeToString(data.getKey()));
                    var nftAttributeMeta = this.nftAttributeMetaService.getOneThrowEx(nftAttributeMetaQueryWrapper);

                    var nftAttributeValue = NftAttributeValue.builder()
                            .nftAttributeMetaId(nftAttributeMeta.getId())
                            .nftMetaId(nftMeta.getId())
                            .build();
                    this.nftAttributeValueService.save(nftAttributeValue);
                }
            }

            var nftSplit = NftSplit.builder()
                    .version(Long.parseLong(transaction.getVersion()))
                    .orderId(nftSplitEvent.getOrderId())
                    .isExecute(nftSplitEvent.isExecute())
                    .owner(nftSplitEvent.getOwner())
                    .metaIds(String.join(",", metaIds))
                    .build();
            nftSplit.setTransactionStatus_(TransactionStatus.STATUS_1_READY);
            nftSplit.setTransactionHash(transaction.getHash());
            this.nftSplitMapper.insert(nftSplit);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addNftComposeEvent(NftComposeEvent nftComposeEvent) {
//        var nftCompose = NftCompose.builder()
//                .orderId(nftComposeEvent.getOrderId())
//                .isExecute(nftComposeEvent.isExecute())
//                .owner(nftComposeEvent.getOwner())
//                .name(nftComposeEvent.getName())
//                .description(nftComposeEvent.getDescription())
//                .build();
//        nftCompose.setTransactionStatus_(TransactionStatus.);
//        nftCompose.setTransactionHash(EMPTY);
//        nftComposeMapper.insert(nftCompose);

        if (nftComposeEvent.isExecute()) {
            //TODO renjian
            //合成记录
        } else {

        }

        return true;
    }

    @Override
    public boolean execute() {
        while (true) {
            if (!this.nftSplit()) {
                break;
            }
        }

        while (true) {
            if (!this.nftCompose()) {
                break;
            }
        }

        return true;
    }

    @Override
    public boolean initialize() {
        var list = this.nftGroupService.list();
        for (NftGroup nftGroup : list) {
            if (nftGroup.getIsInitializeNftOp()) {
                continue;
            }

            if (0L == nftGroup.getSplit()) {
                nftGroup.setIsInitializeNftOp(Boolean.TRUE);
                this.nftGroupService.updateById(nftGroup);
            } else {
                QueryWrapper<NftGroup> nftGroupQueryWrapper = new QueryWrapper<>();
                nftGroupQueryWrapper.lambda().eq(NftGroup::getId, nftGroup.getSplit());
                var nftGroupSplit = this.nftGroupService.getOneThrowEx(nftGroupQueryWrapper);

                QueryWrapper<Language> languageQueryWrapper = new QueryWrapper<>();
                languageQueryWrapper.lambda().eq(Language::getLanguage, com.billion.model.enums.Language.EN.getCode());
                languageQueryWrapper.lambda().eq(Language::getKey, nftGroup.getDisplayName());
                var nameNftGroup = this.languageService.getOneThrowEx(languageQueryWrapper).getValue();

                languageQueryWrapper = new QueryWrapper<>();
                languageQueryWrapper.lambda().eq(Language::getLanguage, com.billion.model.enums.Language.EN.getCode());
                languageQueryWrapper.lambda().eq(Language::getKey, nftGroupSplit.getDisplayName());
                var nameNftGroupSplit = this.languageService.getOneThrowEx(languageQueryWrapper).getValue();

                TransactionPayload transactionPayload = TransactionPayload.builder()
                        .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                        .function(ContextService.getKikoOwner() + "::op_nft::initialize")
                        .arguments(List.of(
                                nftGroup.getOwner(),
                                Hex.encode(nameNftGroup),
                                nftGroupSplit.getOwner(),
                                Hex.encode(nameNftGroupSplit),
                                ContextService.getNftComposeFee()
                        ))
                        .typeArguments(List.of(
                                com.aptos.request.v1.model.Resource.APT().resourceTag()
                        ))
                        .build();

                var response = AptosService.getAptosClient().requestSubmitTransaction(
                        nftGroup.getOwner(),
                        transactionPayload);
                if (response.isValid()) {
                    log.error("{}", response);

                    return false;
                }

                if (!AptosService.checkTransaction(response.getData().getHash())) {
                    log.error("{}", response);

                    return false;
                }

                nftGroup.setIsInitializeNftOp(Boolean.TRUE);
                this.nftGroupService.updateById(nftGroup);
            }
        }

        return true;
    }

    boolean nftSplit() {
        QueryWrapper<NftSplit> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftSplit::getIsExecute, Boolean.FALSE);
        wrapper.lambda().eq(NftSplit::getTransactionStatus, TransactionStatus.STATUS_1_READY.getCode());
        var nftSplit = nftSplitMapper.selectOne(wrapper);
        if (Objects.isNull(nftSplit)) {
            return false;
        }

        var requestAccountResource = AptosService.getAptosClient().requestAccountResource(
                ContextService.getKikoOwner(), com.aptos.request.v1.model.Resource.builder()
                        .moduleAddress(ContextService.getKikoOwner())
                        .moduleName("op_nft")
                        .resourceName("Data")
                        .build());
        if (requestAccountResource.isValid()) {
            return false;
        }

        var opNftData = requestAccountResource.getData().getData().to(OpNftData.class);

        var responseNftSplitEvent = AptosService.getAptosClient().requestTable(
                opNftData.getOrderSpilt().getHandle(),
                TableBody.builder()
                        .keyType("u64")
                        .valueType(ContextService.getKikoOwner() + "::op_nft::NftSplitEvent")
                        .key(nftSplit.getOrderId())
                        .build(),
                NftSplitEvent.class
        );
        if (responseNftSplitEvent.isValid()) {
            return false;
        }

        if (responseNftSplitEvent.getData().isExecute()) {
            nftSplit.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
            nftSplit.setTransactionHash(EMPTY);
            nftSplit.setIsExecute(Boolean.TRUE);
            return true;
        }

        var metaIds = nftSplit.getMetaIds().split(",");
        QueryWrapper<NftMeta> nftMetaQueryWrapper = new QueryWrapper<>();
        nftMetaQueryWrapper.lambda().in(NftMeta::getId, metaIds);
        var nftMetas = this.nftMetaService.list(nftMetaQueryWrapper);
        if (nftMetas.isEmpty()) {
            nftSplit.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            nftSplit.setTransactionHash(EMPTY);
            this.nftSplitMapper.updateById(nftSplit);

            return false;
        }

        List<String> names = new ArrayList<>();
        List<String> descriptions = new ArrayList<>();
        List<String> uris = new ArrayList<>();
        List<String> propertyKeys = new ArrayList<>();

        for (NftMeta nftMeta : nftMetas) {
            QueryWrapper<com.billion.model.entity.Language> languageQueryWrapper = new QueryWrapper<>();
            languageQueryWrapper.lambda().eq(com.billion.model.entity.Language::getLanguage, com.billion.model.enums.Language.EN.getCode());
            languageQueryWrapper.lambda().eq(com.billion.model.entity.Language::getKey, nftMeta.getDisplayName());
            var displayName = this.languageService.getOneThrowEx(languageQueryWrapper).getValue();

            languageQueryWrapper = new QueryWrapper<>();
            languageQueryWrapper.lambda().eq(com.billion.model.entity.Language::getLanguage, com.billion.model.enums.Language.EN.getCode());
            languageQueryWrapper.lambda().eq(com.billion.model.entity.Language::getKey, nftMeta.getDescription());
            var description = this.languageService.getOneThrowEx(languageQueryWrapper).getValue();

            if (StringUtils.isEmpty(displayName)
                    || StringUtils.isEmpty(description)
                    || DEFAULT_TEXT.equals(displayName)
                    || DEFAULT_TEXT.equals(description)
            ) {
                nftSplit.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
                nftSplit.setTransactionHash(EMPTY);
                this.nftSplitMapper.updateById(nftSplit);

                for (NftMeta nftMeta_ : nftMetas) {
                    nftMeta_.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
                    nftMeta_.setTransactionHash(EMPTY);
                    this.nftMetaService.updateById(nftMeta_);
                }

                return false;
            }

            if (26 < displayName.length()) {
                nftSplit.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
                nftSplit.setTransactionHash(EMPTY);
                this.nftSplitMapper.updateById(nftSplit);

                for (NftMeta nftMeta_ : nftMetas) {
                    nftMeta_.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
                    nftMeta_.setTransactionHash(EMPTY);
                    this.nftMetaService.updateById(nftMeta_);
                }

                return false;
            }

            names.add(Hex.encode(displayName));
            descriptions.add(Hex.encode(description));
            uris.add(Hex.encode(nftMeta.getUri()));
            var nftAttributeList = nftAttributeValueService.getNftAttributeForMint(nftMeta.getId());
            if (1 != nftAttributeList.size()) {
                nftSplit.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
                nftSplit.setTransactionHash(EMPTY);
                this.nftSplitMapper.updateById(nftSplit);

                for (NftMeta nftMeta_ : nftMetas) {
                    nftMeta_.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
                    nftMeta_.setTransactionHash(EMPTY);
                    this.nftMetaService.updateById(nftMeta_);
                }

                return false;
            }
            propertyKeys.add(nftAttributeList.get(0).getKey());
        }

        if (nftMetas.size() != names.size()) {
            nftSplit.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            nftSplit.setTransactionHash(EMPTY);
            this.nftSplitMapper.updateById(nftSplit);

            return false;
        }

        TransactionPayload transactionPayload = TransactionPayload.builder()
                .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                .function(ContextService.getKikoOwner() + "::op_nft::split_step2")
                .arguments(List.of(
                        nftSplit.getOrderId(),
                        names,
                        descriptions,
                        uris,
                        propertyKeys
                ))
                .typeArguments(List.of())
                .build();

        var responseTransaction = AptosService.getAptosClient().requestSubmitTransaction(ContextService.getKikoOwner(), transactionPayload);
        if (responseTransaction.isValid()) {
            nftSplit.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            nftSplit.setTransactionHash(EMPTY);
            this.nftSplitMapper.updateById(nftSplit);

            for (NftMeta nftMeta_ : nftMetas) {
                nftMeta_.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
                nftMeta_.setTransactionHash(EMPTY);
                this.nftMetaService.updateById(nftMeta_);
            }

            return false;
        }

        if (!AptosService.checkTransaction(responseTransaction.getData().getHash())) {
            nftSplit.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            nftSplit.setTransactionHash(responseTransaction.getData().getHash());
            this.nftSplitMapper.updateById(nftSplit);

            for (NftMeta nftMeta_ : nftMetas) {
                nftMeta_.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
                nftMeta_.setTransactionHash(responseTransaction.getData().getHash());
                this.nftMetaService.updateById(nftMeta_);
            }

            return false;
        }

        nftSplit.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        nftSplit.setTransactionHash(responseTransaction.getData().getHash());
        nftSplit.setIsExecute(Boolean.TRUE);
        this.nftSplitMapper.updateById(nftSplit);

        for (NftMeta nftMeta_ : nftMetas) {
            nftMeta_.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
            nftMeta_.setTransactionHash(responseTransaction.getData().getHash());
            this.nftMetaService.updateById(nftMeta_);
        }

        //分解记录
        //TODO renjian

        return true;
    }

    boolean nftCompose() {
        QueryWrapper<NftCompose> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftCompose::getIsExecute, Boolean.FALSE);
        wrapper.lambda().eq(NftCompose::getTransactionStatus, TransactionStatus.STATUS_1_READY.getCode());
        var nftCompose = nftComposeMapper.selectOne(wrapper);
        if (Objects.isNull(nftCompose)) {
            return false;
        }

        List<String> propertyKeys = new ArrayList<>();

        TransactionPayload transactionPayload = TransactionPayload.builder()
                .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                .function(ContextService.getKikoOwner() + "::op_nft::compose_step2")
                .arguments(List.of(
                        nftCompose.getOrderId()
                        //uri
                ))
                .typeArguments(List.of())
                .build();

        var response = AptosService.getAptosClient().requestSubmitTransaction(
                ContextService.getKikoOwner(),
                transactionPayload);
        if (response.isValid()) {
            nftCompose.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            nftCompose.setTransactionHash(EMPTY);
            nftComposeMapper.updateById(nftCompose);

            return false;
        }

        if (!AptosService.checkTransaction(response.getData().getHash())) {
            nftCompose.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
            nftCompose.setTransactionHash(response.getData().getHash());
            nftComposeMapper.updateById(nftCompose);

            return false;
        }

        nftCompose.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
        nftCompose.setTransactionHash(response.getData().getHash());
        nftCompose.setIsExecute(Boolean.TRUE);

        nftComposeMapper.updateById(nftCompose);

        return true;
    }

}