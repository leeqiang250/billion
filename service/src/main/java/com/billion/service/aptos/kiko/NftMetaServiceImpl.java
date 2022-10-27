package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.*;
import com.aptos.utils.Hex;
import com.aptos.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.NftMetaMapper;
import com.billion.model.dto.Context;
import com.billion.model.dto.NftMetaDto;
import com.billion.model.entity.*;
import com.billion.model.enums.*;
import com.billion.model.enums.Language;
import com.billion.model.enums.TransactionStatus;
import com.billion.model.exception.BizException;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
import com.billion.service.aptos.ContextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
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
    ImageService imageService;

    @Resource
    HandleService handleService;

    @Resource
    NftGroupService nftGroupService;

    @Resource
    BoxGroupService boxGroupService;

    @Resource
    TokenService tokenService;

    @Resource
    NftAttributeValueService nftAttributeValueService;

    @Resource
    NftService nftService;

    @Resource
    @Lazy
    MarketService marketService;

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
        boxGroupQueryWrapper.lambda().in(BoxGroup::getTransactionStatus, List.of(TransactionStatus.STATUS_1_READY.getCode(), TransactionStatus.STATUS_3_SUCCESS.getCode()));
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

        //TODO leeqiang 是否需要handle 排查
        var handle = handleService.getByAccount(nftGroup.getOwner());
        if (StringUtils.isEmpty(handle.getCollectionsTokenDataHandle())) {
            return false;
        }

        QueryWrapper<com.billion.model.entity.Language> languageQueryWrapper = new QueryWrapper<>();
        languageQueryWrapper.lambda().eq(com.billion.model.entity.Language::getLanguage, Language.EN.getCode());
        languageQueryWrapper.lambda().eq(com.billion.model.entity.Language::getKey, nftGroup.getDisplayName());
        var nftGroupDisplayName = this.languageService.getOneThrowEx(languageQueryWrapper).getValue();
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
        for (NftMeta nftMeta : nftMetas) {
            languageQueryWrapper = new QueryWrapper<>();
            languageQueryWrapper.lambda().eq(com.billion.model.entity.Language::getLanguage, Language.EN.getCode());
            languageQueryWrapper.lambda().eq(com.billion.model.entity.Language::getKey, nftMeta.getDisplayName());
            var displayName = this.languageService.getOneThrowEx(languageQueryWrapper).getValue();

            languageQueryWrapper = new QueryWrapper<>();
            languageQueryWrapper.lambda().eq(com.billion.model.entity.Language::getLanguage, Language.EN.getCode());
            languageQueryWrapper.lambda().eq(com.billion.model.entity.Language::getKey, nftMeta.getDescription());
            var description = this.languageService.getOneThrowEx(languageQueryWrapper).getValue();

            if (StringUtils.isEmpty(displayName)
                    || StringUtils.isEmpty(description)
                    || DEFAULT_TEXT.equals(displayName)
                    || DEFAULT_TEXT.equals(description)
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

            var image = this.imageService.add(nftMeta.getUri(), nftMeta.getClass().getSimpleName() + ":" + nftMeta.getId());

            var nftAttributeList = nftAttributeValueService.getNftAttributeForMint(nftMeta.getId());
            var nftAttributeMap = nftAttributeList.stream().collect(Collectors.toMap(e -> e.getKey(), (e) -> e));
            if (nftAttributeList.size() != nftAttributeMap.size()) {
                nftMeta.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
                nftMeta.setTransactionHash(EMPTY);
                super.updateById(nftMeta);

                throw new BizException("nft attribute error");
            }
            List<String> propertyTypes = new ArrayList<>();
            List<String> propertyKeys = new ArrayList<>();
            List<String> propertyValues = new ArrayList<>();
            nftAttributeList.forEach(nftAttribute -> {
                propertyTypes.add(nftAttribute.getType());
                propertyKeys.add(nftAttribute.getKey());
                propertyValues.add(nftAttribute.getValue());
            });

            TransactionPayload transactionPayload = TransactionPayload.builder()
                    .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                    .function(ContextService.getKikoOwner() + "::help::mint_token_with_box")
                    .arguments(List.of(
                            Hex.encode(nftGroupDisplayName),
                            Hex.encode(displayName),
                            Hex.encode(description),
                            Hex.encode(image.getProxy()),
                            propertyKeys,
                            propertyValues,
                            propertyTypes
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

            nftMeta.setUri(image.getProxy());

            nftMeta.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
            nftMeta.setTransactionHash(response.getData().getHash());

            nftMeta.setTableHandle(handle.getCollectionsTokenDataHandle());
            nftMeta.setTableCollection(Hex.encode(nftGroupDisplayName));
            nftMeta.setTableCreator(nftGroup.getOwner());
            nftMeta.setTableName(Hex.encode(displayName));

            super.updateById(nftMeta);
            this.rank(nftGroupId);
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
    public NftMetaDto getNftMetaInfoById(Context context, String nftMetaId) {
        QueryWrapper<NftMeta> nftMetaQueryWrapper = new QueryWrapper<>();
        nftMetaQueryWrapper.lambda().eq(NftMeta::getId, nftMetaId);

        var nftMeta = this.getOneThrowEx(nftMetaQueryWrapper);
        changeLanguage(context, nftMeta);

//        var nftGroup = nftGroupService.getById(nftMeta.getNftGroupId());

        return this.getNftMetaDto(context, nftMeta);
    }

    @Override
    public NftMetaDto getNftMetaInfoByToken(Context context, String nftTokenId) {
        QueryWrapper<NftMeta> nftMetaQueryWrapper = new QueryWrapper<>();
        nftMetaQueryWrapper.lambda().eq(NftMeta::getTokenId, nftTokenId);

        var nftMeta = this.getOneThrowEx(nftMetaQueryWrapper);
        changeLanguage(context, nftMeta);

        return this.getNftMetaDto(context, nftMeta);
    }

    @Override
    public String getContract(Context context, NftMeta nftMeta) {
        var nftGroup = nftGroupService.getById(nftMeta.getNftGroupId());
        //合约数据
        String contract = nftGroup.getNftContract();
        String[] contractInfos = contract.split("::");
        contract = contractInfos[0] + "::" + changeLanguageContract(context, contractInfos[1]);
        return contract;
    }


    public NftMetaDto getNftMetaDto(Context context, NftMeta nftMeta) {
        NftMetaDto nftMetaDto = NftMetaDto.builder()
                .id(nftMeta.getId())
                .nftGroupId(nftMeta.getNftGroupId())
                .creator(nftMeta.getTokenId().split("@")[0])
                .displayName(nftMeta.getDisplayName())
                .description(nftMeta.getDescription())
                .uri(nftMeta.getUri())
                .rank(nftMeta.getRank())
                .isBorn(nftMeta.getIsBorn())
                .tokenId(nftMeta.getTokenId())
                .score(nftMeta.getScore())
                .attributeType(nftMeta.getAttributeType())
                .owner(nftService.getOwnerByTokenId(context, nftMeta.getTokenId()))
                .build();

        //合约数据
        nftMetaDto.setContract(this.getContract(context, nftMeta));

        //属性数据
        var nftAttributeValues = nftAttributeValueService.getNftAttributeValueByMetaId(context, String.valueOf(nftMeta.getId()));
        //计算score
//        BigDecimal score = nftAttributeValues.stream().map(nftAttribute -> BigDecimal.valueOf(Double.valueOf(nftAttribute.getValue()))).reduce(BigDecimal.ZERO, BigDecimal::add);
//        nftMetaDto.setScore(score.toString());
        nftMetaDto.setAttributeValues(nftAttributeValues);

        //售卖数据
        var marketList = marketService.getMarketListByTokenId(context, nftMetaDto.getTokenId());
        if (Objects.nonNull(marketList) && marketList.size() > 0) {
            Market market = marketList.get(marketList.size() - 1);
            nftMetaDto.setOrderId(market.getOrderId());
            nftMetaDto.setSaleType(market.getType());
            nftMetaDto.setPrice(market.getPrice());
            nftMetaDto.setBidder(market.getBidder());
            nftMetaDto.setBidPrice(market.getBidAmount());
            nftMetaDto.setAuctionPrice(market.getBidAmount());
            nftMetaDto.setTs(market.getTs());
            if (StringUtils.isNotEmpty(market.getBidToken())) {
                String[] tokenInfo = market.getBidToken().split("::");
                nftMetaDto.setBidToken(tokenService.getByTokenInfo(context, tokenInfo[0], tokenInfo[1], tokenInfo[2]));
            }
        }

        return nftMetaDto;
    }
    @Override
    public List<NftMeta> getListByGroup(Context context, String type, String groupId) {
        QueryWrapper<NftMeta> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftMeta::getNftGroupId, groupId);
        //TODO:是否需要状态区别
//        wrapper.lambda().eq(nftMeta::getTransactionStatus, TransactionStatus.STATUS_1_READY.getCode());
        var nftMetas = super.list(wrapper);
        changeLanguage(context, nftMetas);

        return nftMetas;
    }

    @Override
    public List<NftMetaDto> getMyNfts(Context context, String account, String saleState) {
        List<NftMetaDto> resultList = new ArrayList<>();
        if ("unSale".equals(saleState)) {
            resultList = getMyNftsUnSale(context, account);
        }else if ("onSale".equals(saleState)) {
            resultList = getMyNftsOnSale(context, account);
        }
        return resultList;
    }

    public List<NftMetaDto> getMyNftsUnSale(Context context, String account) {
        List<Nft> nftList = nftService.getListByAccount(context, account);
        if (nftList.size() == 0) {
            return new ArrayList<>();
        }
        List<String> tokenIdList = nftList.stream().map(Nft::getTokenId).collect(Collectors.toList());
        var nftMetaList = this.getListByTokenIds(tokenIdList);
        changeLanguage(context, nftMetaList);
        List<NftMetaDto> resultList = new ArrayList<>();
        nftMetaList.forEach(nftMeta -> {
            NftMetaDto nftMetaDto = NftMetaDto.builder()
                    .id(nftMeta.getId())
                    .nftGroupId(nftMeta.getNftGroupId())
                    .displayName(nftMeta.getDisplayName())
                    .description(nftMeta.getDescription())
                    .uri(nftMeta.getUri())
                    .rank(nftMeta.getRank())
                    .isBorn(nftMeta.getIsBorn())
                    .tokenId(nftMeta.getTokenId())
                    .score(nftMeta.getScore())
                    .attributeType(nftMeta.getAttributeType())
                    .owner(account)
                    .build();
            resultList.add(nftMetaDto);
        });

        return resultList;
    }


    public List<NftMetaDto> getMyNftsOnSale(Context context, String account) {
        //查询销售中的NFT
        var marketList = marketService.getMarketListByAccount(context, account, MarketTokenType.NFT.getType());

        var nftMetas = getListByTokenIds(marketList.stream().map(Market::getTokenId).collect(Collectors.toList()));
        changeLanguage(context, nftMetas);
        var nftMetaMap = nftMetas.stream().collect(Collectors.toMap(nftMeta -> nftMeta.getTokenId(), (nftMeta) -> nftMeta));

        List<NftMetaDto> resultList = new ArrayList<>();
        marketList.forEach(nftMarket -> {
            NftMeta nftMeta = nftMetaMap.get(nftMarket.getTokenId());
            NftMetaDto nftMetaDto = NftMetaDto.builder()
                    .id(nftMeta.getId())
                    .nftGroupId(nftMeta.getNftGroupId())
                    .displayName(nftMeta.getDisplayName())
                    .description(nftMeta.getDescription())
                    .uri(nftMeta.getUri())
                    .rank(nftMeta.getRank())
                    .isBorn(nftMeta.getIsBorn())
                    .tokenId(nftMeta.getTokenId())
                    .score(nftMeta.getScore())
                    .attributeType(nftMeta.getAttributeType())
                    .owner(account)
                    .orderId(nftMarket.getOrderId())
                    .saleType(nftMarket.getType())
                    .price(nftMarket.getPrice())
                    .bidder(nftMarket.getBidder())
                    .bidPrice(nftMarket.getBidAmount())
                    .auctionPrice(nftMarket.getBidAmount())
                    .ts(nftMarket.getTs())
                    .deadTs(nftMarket.getDeadTs())
                    .build();
            String bidToken = nftMarket.getBidToken();
            if (StringUtils.isNotEmpty(bidToken)) {
                String[] tokenInfo = bidToken.split("::");
                nftMetaDto.setBidToken(tokenService.getByTokenInfo(context, tokenInfo[0], tokenInfo[1], tokenInfo[2]));
            }
            resultList.add(nftMetaDto);
        });
        return resultList;
    }

    @Override
    public List<NftMeta> getListByTokenIds(List<String> tokenIds) {
        if (Objects.isNull(tokenIds) || tokenIds.size() == 0) {
            return new ArrayList<>();
        }
        QueryWrapper<NftMeta> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(NftMeta::getTokenId, tokenIds);

        return this.list(queryWrapper);
    }

    @Override
    public void rank(Serializable nftGroupId) {
        QueryWrapper<NftMeta> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NftMeta::getNftGroupId, nftGroupId);
        wrapper.lambda().eq(NftMeta::getIsBorn, Boolean.FALSE);
        wrapper.lambda().eq(NftMeta::getTransactionStatus, TransactionStatus.STATUS_3_SUCCESS.getCode());

        var nftMetaList = super.list(wrapper);

        nftMetaList.forEach(nftMeta -> {
            //属性数据
            var nftAttributeValues = nftAttributeValueService.getNftAttributeValueByMetaId(null, String.valueOf(nftMeta.getId()));
            //计算score
            BigDecimal score = nftAttributeValues.stream().map(nftAttribute -> BigDecimal.valueOf(Double.valueOf(nftAttribute.getValue()))).reduce(BigDecimal.ZERO, BigDecimal::add);
            nftMeta.setScore(score.toString());
        });

        nftMetaList = nftMetaList.stream().
                sorted(Comparator.comparing(NftMeta::getScore, Comparator.reverseOrder())).
                collect(Collectors.toList());
        for (int i = 0; i < nftMetaList.size(); i++) {
            NftMeta nftMeta = nftMetaList.get(i);
            nftMeta.setRank(i + 1L);
            this.updateById(nftMeta);
        }
    }


    private void changeLanguage(Context context, List<NftMeta> list) {
        Set setDisplayName = list.stream().map(e -> e.getDisplayName()).collect(Collectors.toSet());
        Set setDescription = list.stream().map(e -> e.getDescription()).collect(Collectors.toSet());

        Map mapDisplayName = this.languageService.getByKeys(context, setDisplayName);
        Map mapDescription = this.languageService.getByKeys(context, setDescription);

        list.forEach(e -> {
            e.setDisplayName(mapDisplayName.get(e.getDisplayName()).toString());
            e.setDescription(mapDescription.get(e.getDescription()).toString());
        });
    }

    private void changeLanguage(Context context, NftMeta nftMeta) {
        nftMeta.setDisplayName(languageService.getByKey(context, nftMeta.getDisplayName()));
        nftMeta.setDescription(languageService.getByKey(context, nftMeta.getDescription()));
    }

    private String changeLanguageContract(Context context, String str) {
        return this.languageService.getByKey(context, str);
    }
}