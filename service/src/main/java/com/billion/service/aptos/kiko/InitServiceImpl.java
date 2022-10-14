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
import java.util.UUID;

import static com.billion.model.constant.RequestPath.EMPTY;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class InitServiceImpl implements InitService {

    final String kiko = "0x1c87ad158f251d661cbacb167e0e459ab1ab43e1a3ca61edbf548f1cc6b23b11";

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
    NftAttributeService nftAttributeService;

    @Resource
    NftClassService nftClassService;

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

    @Override
    public boolean initialize() {
        if (!ContextService.getEnv().equalsIgnoreCase("dev")) {
            return false;
        }

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
        this.nftGroup();
        this.nftMeta();
        this.nftTransfer();
        this.pair();
        this.token();
        this.tokenScene();
        this.tokenTransfer();

        return true;
    }

    void boxGroup() {
        this.boxGroupService.remove(null);

        {
            var boxGroup = BoxGroup.builder()
                    .chain(Chain.APTOS.getCode())
                    .displayName(UUID.randomUUID().toString())
                    .nftGroup(1L)
                    .askToken(2L)
                    .amount("0")
                    .bidToken(1L)
                    .price(String.valueOf(Math.abs(UUID.randomUUID().toString().hashCode()) % 1000 + 1))
                    .description(UUID.randomUUID().toString())
                    .rule(EMPTY)
                    .ts(String.valueOf(System.currentTimeMillis()))
                    .sort(0)
                    .isEnabled(Boolean.TRUE)
                    .transactionHash(EMPTY)
                    .build();
            boxGroup.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);

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
                    .nftGroup(2L)
                    .askToken(3L)
                    .amount("0")
                    .bidToken(1L)
                    .price(String.valueOf(Math.abs(UUID.randomUUID().toString().hashCode()) % 1000 + 1))
                    .description(UUID.randomUUID().toString())
                    .rule(EMPTY)
                    .ts(String.valueOf(System.currentTimeMillis()))
                    .sort(0)
                    .isEnabled(Boolean.TRUE)
                    .transactionHash(EMPTY)
                    .build();
            boxGroup.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);

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
                    .nftGroup(3L)
                    .askToken(4L)
                    .amount("0")
                    .bidToken(1L)
                    .price(String.valueOf(Math.abs(UUID.randomUUID().toString().hashCode()) % 1000 + 1))
                    .description(UUID.randomUUID().toString())
                    .rule(EMPTY)
                    .ts(String.valueOf(System.currentTimeMillis()))
                    .sort(0)
                    .isEnabled(Boolean.TRUE)
                    .transactionHash(EMPTY)
                    .build();

            boxGroup.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);

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
                    .nftGroup(4L)
                    .askToken(5L)
                    .amount("0")
                    .bidToken(1L)
                    .price(String.valueOf(Math.abs(UUID.randomUUID().toString().hashCode()) % 1000 + 1))
                    .description(UUID.randomUUID().toString())
                    .rule(EMPTY)
                    .ts(String.valueOf(System.currentTimeMillis()))
                    .sort(0)
                    .isEnabled(Boolean.TRUE)
                    .transactionHash(EMPTY)
                    .build();
            boxGroup.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);

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
                    .nftGroup(5L)
                    .askToken(6L)
                    .amount("0")
                    .bidToken(7L)
                    .price(String.valueOf(Math.abs(UUID.randomUUID().toString().hashCode()) % 1000 + 1))
                    .description(UUID.randomUUID().toString())
                    .rule(EMPTY)
                    .ts(String.valueOf(System.currentTimeMillis()))
                    .sort(0)
                    .isEnabled(Boolean.TRUE)
                    .transactionHash(EMPTY)
                    .build();

            boxGroup.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);

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
        for (long i = 1; i < 6; i++) {
            var nftGroup = NftGroup.builder()
                    .id(i)
                    .chain(Chain.APTOS.getCode())
                    .split(Boolean.FALSE)
                    .owner(kiko)
                    .meta2(UUID.randomUUID().toString())
                    .body2(UUID.randomUUID().toString())
                    .displayName(UUID.randomUUID().toString())
                    .description(UUID.randomUUID().toString())
                    .currentSupply(EMPTY)
                    .totalSupply(String.valueOf(Long.MAX_VALUE))
                    .uri("https://imagedelivery.net/3mRLd_IbBrrQFSP57PNsVw/76360568-5c54-4342-427d-68992ded7b00/public")
                    .transactionHash(EMPTY)
                    .isEnabled(Boolean.TRUE)
                    .sort(0L)
                    .build();
            nftGroup.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);

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
        }
    }

    void nftMeta() {
        this.nftMetaService.remove(null);

        var nftGroups = this.nftGroupService.list();
        nftGroups.forEach(nftGroup -> {
            for (int i = 0; i < 10; i++) {
                var nftMeta = NftMeta.builder()
                        .boxGroupId(0L)
                        .nftGroupId(nftGroup.getId())
                        .displayName(UUID.randomUUID().toString())
                        .description(UUID.randomUUID().toString())
                        .uri("https://imagedelivery.net/3mRLd_IbBrrQFSP57PNsVw/4031cc60-3e88-4f78-b412-5006ecf5c100/public")
                        .rank(0L)
                        .transactionHash(EMPTY)
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
                    .transactionHash(EMPTY)
                    .isShow(Boolean.TRUE)
                    .build();
            token.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_3_SUCCESS);

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
                    .transactionHash(EMPTY)
                    .isShow(Boolean.TRUE)
                    .build();
            token.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);

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
                    .transactionHash(EMPTY)
                    .isShow(Boolean.TRUE)
                    .build();
            token.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);

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
                    .transactionHash(EMPTY)
                    .isShow(Boolean.TRUE)
                    .build();
            token.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);

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
                    .transactionHash(EMPTY)
                    .isShow(Boolean.TRUE)
                    .build();
            token.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);

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
                    .transactionHash(EMPTY)
                    .isShow(Boolean.TRUE)
                    .build();
            token.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);

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
                    .transactionHash(EMPTY)
                    .isShow(Boolean.TRUE)
                    .build();
            token.setTransactionStatus_(com.billion.model.enums.TransactionStatus.STATUS_1_READY);

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

    void tokenTransfer() {
        this.tokenTransferService.remove(null);
    }

}