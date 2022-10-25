package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.TokenDataId;
import com.aptos.request.v1.model.TokenId;
import com.aptos.utils.Hex;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.TokenSceneMapper;
import com.billion.model.entity.*;
import com.billion.model.enums.Chain;
import com.billion.service.aptos.ContextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.billion.model.constant.RequestPath.EMPTY;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class InitServiceImpl implements InitService {

    final String kiko = "0xa9e93a5297a5ee85445c52daabf0d7a8cf92f770a12e3a621690d050b2bd7e5d";

    @Resource
    BoxGroupService boxGroupService;

    @Resource
    ConfigService configService;

    @Resource
    ContractService contractService;

    @Resource
    HandleService handleService;

    @Resource
    ImageService imageService;

    @Resource
    LanguageService languageService;

    @Resource
    LogChainService logChainService;

    @Resource
    MarketService marketService;

    @Resource
    NftService nftService;

    @Resource
    NftGroupService nftGroupService;

    @Resource
    NftMetaService nftMetaService;

    @Resource
    NftTransferService nftTransferService;

    @Resource
    PairService pairService;

    @Resource
    TokenService tokenService;

    @Resource
    TokenSceneMapper tokenSceneMapper;

    @Resource
    TokenTransferService tokenTransferService;

    @Resource
    NftAttributeTypeService nftAttributeTypeService;

    @Resource
    NftAttributeMetaService nftAttributeMetaService;

    @Resource
    NftAttributeValueService nftAttributeValueService;

    @Resource
    NftSplitService nftSplitService;

    @Resource
    NftComposeService nftComposeService;

    @Resource
    OperationService operationService;

    @Override
    public boolean initialize() {
        if (!ContextService.getEnv().equalsIgnoreCase("dev")) {
            return false;
        }

        this.nftGroup();
        this.boxGroup();
        this.config();
        this.contract();
        this.handle();
        //image
        //language
        this.logChain();
        this.market();
        this.nft();
        //nftAttributeService
        //nftClassService
        this.nftMeta();
        this.nftAttribute();
        this.nftTransfer();
        this.pair();
        this.token();
        this.tokenScene();
        this.nftSplit();
        this.nftCompose();
        this.operation();
        this.tokenTransfer();

        return true;
    }

    void boxGroup() {
        this.boxGroupService.remove(null);

        var list = nftGroupService.list();

        {
            var boxGroup = BoxGroup.builder()
                    .chain(Chain.APTOS.getCode())
                    .displayName(UUID.randomUUID().toString())
                    .nftGroup(list.get(0).getId())
                    .askToken(2L)
                    .amount("0")
                    .bidToken(1L)
                    .price(String.valueOf(Math.abs(UUID.randomUUID().toString().hashCode()) % 1000 + 1))
                    .description(UUID.randomUUID().toString())
                    .rule(EMPTY)
                    .uri("https://imagedelivery.net/3mRLd_IbBrrQFSP57PNsVw/87b47f3a-c033-47ea-0096-40a4ea1fab00/public")
                    .ts(String.valueOf(System.currentTimeMillis()))
                    .sort(0)
                    .isEnabled(Boolean.TRUE)
                    .build();
            boxGroup.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);
            boxGroup.setTransactionHash(EMPTY);

            this.boxGroupService.save(boxGroup);

            this.languageService.save(Language.builder()
                    .language(com.billion.model.enums.Language.EN.getCode())
                    .key(boxGroup.getDisplayName())
                    .value("名称" + boxGroup.getId())
                    .build());

            this.languageService.save(Language.builder()
                    .language(com.billion.model.enums.Language.EN.getCode())
                    .key(boxGroup.getDescription())
                    .value("描述" + boxGroup.getId())
                    .build());
        }
        {
            var boxGroup = BoxGroup.builder()
                    .chain(Chain.APTOS.getCode())
                    .displayName(UUID.randomUUID().toString())
                    .nftGroup(list.get(1).getId())
                    .askToken(3L)
                    .amount("0")
                    .bidToken(1L)
                    .price(String.valueOf(Math.abs(UUID.randomUUID().toString().hashCode()) % 1000 + 1))
                    .description(UUID.randomUUID().toString())
                    .rule(EMPTY)
                    .uri("https://imagedelivery.net/3mRLd_IbBrrQFSP57PNsVw/87b47f3a-c033-47ea-0096-40a4ea1fab00/public")
                    .ts(String.valueOf(System.currentTimeMillis()))
                    .sort(0)
                    .isEnabled(Boolean.TRUE)
                    .build();
            boxGroup.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);
            boxGroup.setTransactionHash(EMPTY);

            this.boxGroupService.save(boxGroup);

            this.languageService.save(Language.builder()
                    .language(com.billion.model.enums.Language.EN.getCode())
                    .key(boxGroup.getDisplayName())
                    .value("名称" + boxGroup.getId())
                    .build());

            this.languageService.save(Language.builder()
                    .language(com.billion.model.enums.Language.EN.getCode())
                    .key(boxGroup.getDescription())
                    .value("描述" + boxGroup.getId())
                    .build());
        }
        {
            var boxGroup = BoxGroup.builder()
                    .chain(Chain.APTOS.getCode())
                    .displayName(UUID.randomUUID().toString())
                    .nftGroup(list.get(2).getId())
                    .askToken(4L)
                    .amount("0")
                    .bidToken(1L)
                    .price(String.valueOf(Math.abs(UUID.randomUUID().toString().hashCode()) % 1000 + 1))
                    .description(UUID.randomUUID().toString())
                    .rule(EMPTY)
                    .uri("https://imagedelivery.net/3mRLd_IbBrrQFSP57PNsVw/87b47f3a-c033-47ea-0096-40a4ea1fab00/public")
                    .ts(String.valueOf(System.currentTimeMillis()))
                    .sort(0)
                    .isEnabled(Boolean.TRUE)
                    .build();
            boxGroup.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);
            boxGroup.setTransactionHash(EMPTY);

            this.boxGroupService.save(boxGroup);

            this.languageService.save(Language.builder()
                    .language(com.billion.model.enums.Language.EN.getCode())
                    .key(boxGroup.getDisplayName())
                    .value("名称" + boxGroup.getId())
                    .build());

            this.languageService.save(Language.builder()
                    .language(com.billion.model.enums.Language.EN.getCode())
                    .key(boxGroup.getDescription())
                    .value("描述" + boxGroup.getId())
                    .build());
        }
        {
            var boxGroup = BoxGroup.builder()
                    .chain(Chain.APTOS.getCode())
                    .displayName(UUID.randomUUID().toString())
                    .nftGroup(list.get(3).getId())
                    .askToken(5L)
                    .amount("0")
                    .bidToken(1L)
                    .price(String.valueOf(Math.abs(UUID.randomUUID().toString().hashCode()) % 1000 + 1))
                    .description(UUID.randomUUID().toString())
                    .rule(EMPTY)
                    .uri("https://imagedelivery.net/3mRLd_IbBrrQFSP57PNsVw/87b47f3a-c033-47ea-0096-40a4ea1fab00/public")
                    .ts(String.valueOf(System.currentTimeMillis()))
                    .sort(0)
                    .isEnabled(Boolean.TRUE)
                    .build();
            boxGroup.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);
            boxGroup.setTransactionHash(EMPTY);

            this.boxGroupService.save(boxGroup);

            this.languageService.save(Language.builder()
                    .language(com.billion.model.enums.Language.EN.getCode())
                    .key(boxGroup.getDisplayName())
                    .value("名称" + boxGroup.getId())
                    .build());

            this.languageService.save(Language.builder()
                    .language(com.billion.model.enums.Language.EN.getCode())
                    .key(boxGroup.getDescription())
                    .value("描述" + boxGroup.getId())
                    .build());
        }
        {
            var boxGroup = BoxGroup.builder()
                    .chain(Chain.APTOS.getCode())
                    .displayName(UUID.randomUUID().toString())
                    .nftGroup(list.get(4).getId())
                    .askToken(6L)
                    .amount("0")
                    .bidToken(7L)
                    .price(String.valueOf(Math.abs(UUID.randomUUID().toString().hashCode()) % 1000 + 1))
                    .description(UUID.randomUUID().toString())
                    .rule(EMPTY)
                    .uri("https://imagedelivery.net/3mRLd_IbBrrQFSP57PNsVw/87b47f3a-c033-47ea-0096-40a4ea1fab00/public")
                    .ts(String.valueOf(System.currentTimeMillis()))
                    .sort(0)
                    .isEnabled(Boolean.TRUE)
                    .build();
            boxGroup.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);
            boxGroup.setTransactionHash(EMPTY);

            this.boxGroupService.save(boxGroup);

            this.languageService.save(Language.builder()
                    .language(com.billion.model.enums.Language.EN.getCode())
                    .key(boxGroup.getDisplayName())
                    .value("名称" + boxGroup.getId())
                    .build());

            this.languageService.save(Language.builder()
                    .language(com.billion.model.enums.Language.EN.getCode())
                    .key(boxGroup.getDescription())
                    .value("描述" + boxGroup.getId())
                    .build());
        }
    }

    void config() {
        this.configService.remove(null);
    }

    void contract() {
        this.contractService.remove(null);

        this.contractService.save(Contract.builder()
                .chain(Chain.APTOS.getCode())
                .name("kiko_owner")
                .moduleAddress(kiko)
                .moduleName(EMPTY)
                .build());

        this.contractService.save(Contract.builder()
                .chain(Chain.APTOS.getCode())
                .name("primary_market")
                .moduleAddress(kiko)
                .moduleName("primary_market")
                .build());

        this.contractService.save(Contract.builder()
                .chain(Chain.APTOS.getCode())
                .name("secondary_market")
                .moduleAddress(kiko)
                .moduleName("secondary_market")
                .build());
    }

    void handle() {
        this.handleService.remove(null);
    }

    void logChain() {
        this.logChainService.remove(null);
    }

    void market() {
        this.marketService.remove(null);
    }

    void nft() {
        this.nftService.remove(null);
    }

    void nftGroup() {
        this.nftGroupService.remove(null);
        for (long i = 1; i < 4; i++) {
            var nftGroup = NftGroup.builder()
                    .chain(Chain.APTOS.getCode())
                    .split(0L)
                    .owner(kiko)
                    .displayName(UUID.randomUUID().toString())
                    .description(UUID.randomUUID().toString())
                    .currentSupply(EMPTY)
                    .totalSupply(String.valueOf(Long.MAX_VALUE))
                    .uri("https://imagedelivery.net/3mRLd_IbBrrQFSP57PNsVw/76360568-5c54-4342-427d-68992ded7b00/public")
                    .isEnabled(Boolean.TRUE)
                    .isInitializeNftOp(Boolean.FALSE)
                    .sort(0L)
                    .bodyQiyong(UUID.randomUUID().toString())
                    .metaQiyong(UUID.randomUUID().toString())
                    .build();
            nftGroup.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);
            nftGroup.setTransactionHash(EMPTY);

            this.nftGroupService.save(nftGroup);

            this.languageService.save(Language.builder()
                    .language(com.billion.model.enums.Language.EN.getCode())
                    .key(nftGroup.getDisplayName())
                    .value("名称" + nftGroup.getId())
                    .build());

            this.languageService.save(Language.builder()
                    .language(com.billion.model.enums.Language.EN.getCode())
                    .key(nftGroup.getDescription())
                    .value("描述" + nftGroup.getId())
                    .build());

            var nftGroup2 = NftGroup.builder()
                    .chain(Chain.APTOS.getCode())
                    .split(nftGroup.getId())
                    .owner(kiko)
                    .displayName(UUID.randomUUID().toString())
                    .description(UUID.randomUUID().toString())
                    .currentSupply(EMPTY)
                    .totalSupply(String.valueOf(Long.MAX_VALUE))
                    .uri("https://imagedelivery.net/3mRLd_IbBrrQFSP57PNsVw/76360568-5c54-4342-427d-68992ded7b00/public")
                    .isEnabled(Boolean.TRUE)
                    .isInitializeNftOp(Boolean.FALSE)
                    .sort(0L)
                    .bodyQiyong(UUID.randomUUID().toString())
                    .metaQiyong(UUID.randomUUID().toString())
                    .build();
            nftGroup2.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);
            nftGroup2.setTransactionHash(EMPTY);

            this.nftGroupService.save(nftGroup2);

            this.languageService.save(Language.builder()
                    .language(com.billion.model.enums.Language.EN.getCode())
                    .key(nftGroup2.getDisplayName())
                    .value("名称" + nftGroup2.getId())
                    .build());

            this.languageService.save(Language.builder()
                    .language(com.billion.model.enums.Language.EN.getCode())
                    .key(nftGroup2.getDescription())
                    .value("描述" + nftGroup2.getId())
                    .build());
        }
    }

    void nftMeta() {
        this.nftMetaService.remove(null);

        var nftGroups = this.nftGroupService.list();
        nftGroups.forEach(nftGroup -> {
            int total = 2;
            if (0 == nftGroup.getId() % 2) {
                total = 1;
            }

            for (int i = 0; i < total; i++) {
                var nftMeta = NftMeta.builder()
                        .nftGroupId(nftGroup.getId())
                        .displayName(UUID.randomUUID().toString())
                        .description(UUID.randomUUID().toString())
                        .uri("https://imagedelivery.net/3mRLd_IbBrrQFSP57PNsVw/4031cc60-3e88-4f78-b412-5006ecf5c100/public")
                        .rank(0L)
                        .isBorn(Boolean.FALSE)
                        .tokenId(EMPTY)
                        .score(EMPTY)
                        .attributeType(0)
                        .tableHandle(EMPTY)
                        .tableCollection(EMPTY)
                        .tableCreator(EMPTY)
                        .tableName(EMPTY)
                        .build();
                nftMeta.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);
                nftMeta.setTransactionHash(EMPTY);

                nftMetaService.save(nftMeta);

                languageService.save(Language.builder()
                        .language(com.billion.model.enums.Language.EN.getCode())
                        .key(nftMeta.getDisplayName())
                        .value(nftGroup.getDisplayName().substring(0, 2) + "-描述" + nftMeta.getId())
                        .build());

                languageService.save(Language.builder()
                        .language(com.billion.model.enums.Language.EN.getCode())
                        .key(nftMeta.getDescription())
                        .value(nftGroup.getDescription().substring(0, 2) + "-描述" + nftMeta.getId())
                        .build());


                QueryWrapper<Language> languageQueryWrapper = new QueryWrapper<>();
                languageQueryWrapper.lambda().eq(Language::getKey, nftMeta.getDisplayName());
                languageQueryWrapper.lambda().eq(Language::getLanguage, com.billion.model.enums.Language.EN.getCode());
                var languageNftMeta = languageService.getOneThrowEx(languageQueryWrapper);

                languageQueryWrapper = new QueryWrapper<>();
                languageQueryWrapper.lambda().eq(Language::getKey, nftGroup.getDisplayName());
                languageQueryWrapper.lambda().eq(Language::getLanguage, com.billion.model.enums.Language.EN.getCode());
                var languageNftGroup = languageService.getOneThrowEx(languageQueryWrapper);

                var tokenId = TokenId.builder()
                        .tokenDataId(TokenDataId.builder()
                                .creator(nftGroup.getOwner())
                                .collection(Hex.encode(languageNftGroup.getValue()))
                                .name(Hex.encode(languageNftMeta.getValue()))
                                .build())
                        .propertyVersion("0")
                        .build();
                nftMeta.setTokenId(tokenId.getNftTokenIdKey());

                nftMetaService.updateById(nftMeta);
            }
        });
    }

    void nftAttribute() {
        this.nftAttributeTypeService.remove(null);
        this.nftAttributeMetaService.remove(null);
        this.nftAttributeValueService.remove(null);
        Map<NftAttributeType, List<NftAttributeMeta>> typeMap = new HashMap<>();
        List<NftAttributeMeta> attributeMetaList = new ArrayList<>();
        List<NftAttributeMeta> attributeMetaList1 = new ArrayList<>();
        List<NftAttributeMeta> attributeMetaList2 = new ArrayList<>();
        var nftGroups = this.nftGroupService.list();
        nftGroups.forEach(nftGroup -> {
            typeMap.clear();
            attributeMetaList.clear();
            attributeMetaList1.clear();
            attributeMetaList2.clear();
            //衣服
            NftAttributeType nftAttributeType = NftAttributeType.builder()
                    .nftGroupId(nftGroup.getId())
                    .className(EMPTY)
                    .build();
            this.nftAttributeTypeService.save(nftAttributeType);

            languageService.save(Language.builder()
                    .language(com.billion.model.enums.Language.EN.getCode())
                    .key("nft_attribute_clothes_" + nftAttributeType.getId())
                    .value("衣服")
                    .build());

            nftAttributeType.setClassName("nft_attribute_clothes_" + nftAttributeType.getId());
            this.nftAttributeTypeService.updateById(nftAttributeType);
            //nftAttributeMeta数据
            NftAttributeMeta nftAttributeMeta = NftAttributeMeta.builder()
                    .nftAttributeTypeId(nftAttributeType.getId())
                    .attribute(EMPTY)
                    .value("10")
                    .uri(EMPTY)
                    .sort(EMPTY)
                    .build();
            this.nftAttributeMetaService.save(nftAttributeMeta);
            languageService.save(Language.builder()
                    .language(com.billion.model.enums.Language.EN.getCode())
                    .key("nft_attribute_clothes_blue" + nftAttributeMeta.getId())
                    .value("蓝色")
                    .build());
            nftAttributeMeta.setAttribute("nft_attribute_clothes_blue" + nftAttributeMeta.getId());
            this.nftAttributeMetaService.updateById(nftAttributeMeta);
            attributeMetaList.add(nftAttributeMeta);

            NftAttributeMeta nftAttributeMeta2 = NftAttributeMeta.builder()
                    .nftAttributeTypeId(nftAttributeType.getId())
                    .attribute(EMPTY)
                    .value("20")
                    .uri(EMPTY)
                    .sort(EMPTY)
                    .build();
            this.nftAttributeMetaService.save(nftAttributeMeta2);
            languageService.save(Language.builder()
                    .language(com.billion.model.enums.Language.EN.getCode())
                    .key("nft_attribute_clothes_red" + nftAttributeMeta2.getId())
                    .value("红色")
                    .build());
            nftAttributeMeta2.setAttribute("nft_attribute_clothes_red" + nftAttributeMeta2.getId());
            this.nftAttributeMetaService.updateById(nftAttributeMeta2);
            attributeMetaList.add(nftAttributeMeta2);

            NftAttributeMeta nftAttributeMeta3 = NftAttributeMeta.builder()
                    .nftAttributeTypeId(nftAttributeType.getId())
                    .attribute(EMPTY)
                    .value("30")
                    .uri(EMPTY)
                    .sort(EMPTY)
                    .build();
            this.nftAttributeMetaService.save(nftAttributeMeta3);
            languageService.save(Language.builder()
                    .language(com.billion.model.enums.Language.EN.getCode())
                    .key("nft_attribute_clothes_yellow" + nftAttributeMeta3.getId())
                    .value("黄色")
                    .build());
            nftAttributeMeta3.setAttribute("nft_attribute_clothes_yellow" + nftAttributeMeta3.getId());
            this.nftAttributeMetaService.updateById(nftAttributeMeta3);
            attributeMetaList.add(nftAttributeMeta3);
            typeMap.put(nftAttributeType, attributeMetaList);

            //性别
            NftAttributeType nftAttributeType2 = NftAttributeType.builder()
                    .nftGroupId(nftGroup.getId())
                    .className(EMPTY)
                    .build();
            this.nftAttributeTypeService.save(nftAttributeType2);

            languageService.save(Language.builder()
                    .language(com.billion.model.enums.Language.EN.getCode())
                    .key("nft_attribute_sex_" + nftAttributeType2.getId())
                    .value("性别")
                    .build());

            nftAttributeType2.setClassName("nft_attribute_sex_" + nftAttributeType2.getId());
            this.nftAttributeTypeService.updateById(nftAttributeType2);
            //nftAttributeMeta数据
            NftAttributeMeta nftAttributeMeta4 = NftAttributeMeta.builder()
                    .nftAttributeTypeId(nftAttributeType2.getId())
                    .attribute(EMPTY)
                    .value("20")
                    .uri(EMPTY)
                    .sort(EMPTY)
                    .build();
            this.nftAttributeMetaService.save(nftAttributeMeta4);
            languageService.save(Language.builder()
                    .language(com.billion.model.enums.Language.EN.getCode())
                    .key("nft_attribute_sex_boy" + nftAttributeMeta4.getId())
                    .value("男")
                    .build());
            nftAttributeMeta4.setAttribute("nft_attribute_sex_boy" + nftAttributeMeta4.getId());
            this.nftAttributeMetaService.updateById(nftAttributeMeta4);
            attributeMetaList1.add(nftAttributeMeta4);

            NftAttributeMeta nftAttributeMeta5 = NftAttributeMeta.builder()
                    .nftAttributeTypeId(nftAttributeType2.getId())
                    .attribute(EMPTY)
                    .value("10")
                    .uri(EMPTY)
                    .sort(EMPTY)
                    .build();
            this.nftAttributeMetaService.save(nftAttributeMeta5);
            languageService.save(Language.builder()
                    .language(com.billion.model.enums.Language.EN.getCode())
                    .key("nft_attribute_sex_girl" + nftAttributeMeta5.getId())
                    .value("女")
                    .build());
            nftAttributeMeta5.setAttribute("nft_attribute_sex_girl" + nftAttributeMeta5.getId());
            this.nftAttributeMetaService.updateById(nftAttributeMeta5);
            attributeMetaList1.add(nftAttributeMeta5);
            typeMap.put(nftAttributeType2, attributeMetaList1);

            //肤色
            NftAttributeType nftAttributeType3 = NftAttributeType.builder()
                    .nftGroupId(nftGroup.getId())
                    .className(EMPTY)
                    .build();
            this.nftAttributeTypeService.save(nftAttributeType3);

            languageService.save(Language.builder()
                    .language(com.billion.model.enums.Language.EN.getCode())
                    .key("nft_attribute_skin_" + nftAttributeType3.getId())
                    .value("肤色")
                    .build());

            nftAttributeType3.setClassName("nft_attribute_skin_" + nftAttributeType3.getId());
            this.nftAttributeTypeService.updateById(nftAttributeType3);

            NftAttributeMeta nftAttributeMeta6 = NftAttributeMeta.builder()
                    .nftAttributeTypeId(nftAttributeType3.getId())
                    .attribute(EMPTY)
                    .value("20")
                    .uri(EMPTY)
                    .sort(EMPTY)
                    .build();
            this.nftAttributeMetaService.save(nftAttributeMeta6);
            languageService.save(Language.builder()
                    .language(com.billion.model.enums.Language.EN.getCode())
                    .key("nft_attribute_skin_yellow" + nftAttributeMeta6.getId())
                    .value("黄色")
                    .build());
            nftAttributeMeta6.setAttribute("nft_attribute_skin_yellow" + nftAttributeMeta6.getId());
            this.nftAttributeMetaService.updateById(nftAttributeMeta6);
            attributeMetaList2.add(nftAttributeMeta6);

            NftAttributeMeta nftAttributeMeta7 = NftAttributeMeta.builder()
                    .nftAttributeTypeId(nftAttributeType3.getId())
                    .attribute(EMPTY)
                    .value("10")
                    .uri(EMPTY)
                    .sort(EMPTY)
                    .build();
            this.nftAttributeMetaService.save(nftAttributeMeta7);
            languageService.save(Language.builder()
                    .language(com.billion.model.enums.Language.EN.getCode())
                    .key("nft_attribute_skin_white" + nftAttributeMeta7.getId())
                    .value("白色")
                    .build());
            nftAttributeMeta7.setAttribute("nft_attribute_skin_white" + nftAttributeMeta7.getId());
            this.nftAttributeMetaService.updateById(nftAttributeMeta7);
            attributeMetaList2.add(nftAttributeMeta7);
            typeMap.put(nftAttributeType3, attributeMetaList2);

            //构造nftmet属性
            QueryWrapper<NftMeta> nftMetaQueryWrapper = new QueryWrapper<>();
            nftMetaQueryWrapper.lambda().eq(NftMeta::getNftGroupId, nftGroup.getId());

            var nftMetas = this.nftMetaService.list(nftMetaQueryWrapper);
            List<List<NftAttributeMeta>> metaList = typeMap.values().stream().collect(Collectors.toList());
            nftMetas.forEach(nftMeta -> {
                Set<Integer> randomSet = new HashSet(3);
                int typeNum = new Random().nextInt(3);
                for (int i = 0; i <= typeNum; i++) {
                    Integer random = new Random().nextInt(metaList.size());


                    if (randomSet.contains(random)) {
                        while (true) {
                            random = new Random().nextInt(metaList.size());
                            if (!randomSet.contains(random)) {
                                break;
                            }
                        }
                    }
                    randomSet.add(random);
                    var metas = metaList.get(random);//随机取某个类型的属性list
                    //nftAttributeValue
                    NftAttributeValue nftAttributeValue = NftAttributeValue.builder()
                            .nftAttributeMetaId(metas.get(new Random().nextInt(metas.size())).getId())
                            .nftMetaId(nftMeta.getId())
                            .build();
                    this.nftAttributeValueService.save(nftAttributeValue);
                }
            });
        });
    }

    void nftTransfer() {
        this.nftTransferService.remove(null);
    }

    void pair() {
        this.pairService.remove(null);
    }

    void token() {
        this.tokenService.remove(null);

        {
            var token = Token.builder()
                    .id(1L)
                    .chain(Chain.APTOS.getCode())
                    .moduleAddress("0x1")
                    .moduleName("aptos_coin")
                    .structName("AptosCoin")
                    .name("AptosCoin")
                    .symbol("AptosCoin")
                    .decimals(8)
                    .displayDecimals(2)
                    .uri(EMPTY)
                    .isShow(Boolean.TRUE)
                    .build();
            token.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_3_SUCCESS);
            token.setTransactionHash(EMPTY);

            this.tokenService.save(token);
        }
        {
            var token = Token.builder()
                    .id(2L)
                    .chain(Chain.APTOS.getCode())
                    .moduleAddress(kiko)
                    .moduleName("box")
                    .structName("BoxV1")
                    .name("BoxV1")
                    .symbol("BoxV1")
                    .decimals(0)
                    .displayDecimals(0)
                    .uri(EMPTY)
                    .isShow(Boolean.TRUE)
                    .build();
            token.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);
            token.setTransactionHash(EMPTY);

            this.tokenService.save(token);
        }
        {
            var token = Token.builder()
                    .id(3L)
                    .chain(Chain.APTOS.getCode())
                    .moduleAddress(kiko)
                    .moduleName("box")
                    .structName("BoxV2")
                    .name("BoxV2")
                    .symbol("BoxV2")
                    .decimals(0)
                    .displayDecimals(0)
                    .uri(EMPTY)
                    .isShow(Boolean.TRUE)
                    .build();
            token.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);
            token.setTransactionHash(EMPTY);

            this.tokenService.save(token);
        }
        {
            var token = Token.builder()
                    .id(4L)
                    .chain(Chain.APTOS.getCode())
                    .moduleAddress(kiko)
                    .moduleName("box")
                    .structName("BoxV3")
                    .name("BoxV3")
                    .symbol("BoxV3")
                    .decimals(0)
                    .displayDecimals(0)
                    .uri(EMPTY)
                    .isShow(Boolean.TRUE)
                    .build();
            token.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);
            token.setTransactionHash(EMPTY);

            this.tokenService.save(token);
        }
        {
            var token = Token.builder()
                    .id(5L)
                    .chain(Chain.APTOS.getCode())
                    .moduleAddress(kiko)
                    .moduleName("box")
                    .structName("BoxV4")
                    .name("BoxV4")
                    .symbol("BoxV4")
                    .decimals(0)
                    .displayDecimals(0)
                    .uri(EMPTY)
                    .isShow(Boolean.TRUE)
                    .build();
            token.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);
            token.setTransactionHash(EMPTY);

            this.tokenService.save(token);
        }
        {
            var token = Token.builder()
                    .id(6L)
                    .chain(Chain.APTOS.getCode())
                    .moduleAddress(kiko)
                    .moduleName("box")
                    .structName("BoxV5")
                    .name("BoxV5")
                    .symbol("BoxV5")
                    .decimals(0)
                    .displayDecimals(0)
                    .uri(EMPTY)
                    .isShow(Boolean.TRUE)
                    .build();
            token.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);
            token.setTransactionHash(EMPTY);

            this.tokenService.save(token);
        }
        {
            var token = Token.builder()
                    .id(7L)
                    .chain(Chain.APTOS.getCode())
                    .moduleAddress(kiko)
                    .moduleName("box")
                    .structName("Win")
                    .name("Win")
                    .symbol("WIN")
                    .decimals(8)
                    .displayDecimals(2)
                    .uri(EMPTY)
                    .isShow(Boolean.TRUE)
                    .build();
            token.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);
            token.setTransactionHash(EMPTY);

            this.tokenService.save(token);
        }
    }

    void tokenScene() {
        this.tokenSceneMapper.delete(null);

        var tokenScene = TokenScene.builder()
                .tokenId(1L)
                .scene("market")
                .build();
        this.tokenSceneMapper.insert(tokenScene);

        tokenScene = TokenScene.builder()
                .tokenId(7L)
                .scene("market")
                .build();
        this.tokenSceneMapper.insert(tokenScene);
    }

    void nftSplit() {
        this.nftSplitService.remove(null);
    }

    void nftCompose() {
        this.nftComposeService.remove(null);
    }

    void operation() {
        this.operationService.remove(null);
    }

    void tokenTransfer() {
        this.tokenTransferService.remove(null);
    }

}