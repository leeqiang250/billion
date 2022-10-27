package com.billion.service.aptos.kiko;

import com.aptos.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.framework.util.Excel;
import com.billion.framework.util.Image;
import com.billion.model.entity.*;
import com.billion.model.enums.Chain;
import com.billion.model.enums.TransactionStatus;
import com.billion.service.aptos.ContextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.billion.model.constant.RequestPath.EMPTY;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class MintServiceImpl implements MintService {

    @Resource
    TokenService tokenService;

    @Resource
    NftGroupService nftGroupService;

    @Resource
    NftMetaService nftMetaService;

    @Resource
    BoxGroupService boxGroupService;

    @Resource
    NftOpService nftOpService;

    @Resource
    LanguageService languageService;

    @Resource
    NftAttributeTypeService nftAttributeTypeService;

    @Resource
    NftAttributeMetaService nftAttributeMetaService;

    @Resource
    NftAttributeValueService nftAttributeValueService;

    @Override
    public boolean initialize(Serializable boxGroupId) {
        if (!this.tokenService.initialize()) {
            return false;
        }

        QueryWrapper<BoxGroup> boxGroupQueryWrapper = new QueryWrapper<>();
        boxGroupQueryWrapper.lambda().eq(BoxGroup::getId, boxGroupId);
        boxGroupQueryWrapper.lambda().eq(BoxGroup::getChain, Chain.APTOS.getCode());
        boxGroupQueryWrapper.lambda().eq(BoxGroup::getIsEnabled, Boolean.TRUE);
        boxGroupQueryWrapper.lambda().in(BoxGroup::getTransactionStatus, List.of(TransactionStatus.STATUS_1_READY.getCode(), TransactionStatus.STATUS_3_SUCCESS.getCode()));
        var boxGroup = this.boxGroupService.getOneThrowEx(boxGroupQueryWrapper);

        if (!this.nftGroupService.initialize(boxGroup.getNftGroup())) {
            return false;
        }

        if (!this.nftMetaService.initialize(boxGroup.getId(), boxGroup.getNftGroup())) {
            return false;
        }

        if (!this.boxGroupService.initialize(boxGroup.getId())) {
            return false;
        }

        if (!this.nftOpService.initialize()) {
            return false;
        }

        return true;
    }

    public boolean importNftImage2Db(long nftGroupId) {
        var group = new File(ContextService.getKikoNftImagePath() + nftGroupId);
        if (!group.isDirectory()) {
            return false;
        }

        var png = ".png";
        for (var file : group.listFiles()) {
            if (file.isDirectory()) {
                var className = nftGroupId + ":" + file.getName();
                var nftAttributeTypeQueryWrapper = new QueryWrapper<NftAttributeType>();
                nftAttributeTypeQueryWrapper.lambda().eq(NftAttributeType::getNftGroupId, nftGroupId);
                nftAttributeTypeQueryWrapper.lambda().eq(NftAttributeType::getClassName, className);
                var nftAttributeType = this.nftAttributeTypeService.getOne(nftAttributeTypeQueryWrapper, false);
                if (Objects.isNull(nftAttributeType)) {
                    nftAttributeType = NftAttributeType.builder()
                            .nftGroupId(nftGroupId)
                            .className(className)
                            .build();
                    this.nftAttributeTypeService.save(nftAttributeType);
                    log.info("{}", nftAttributeType);
                }

                for (var language : com.billion.model.enums.Language.values()) {
                    if (Objects.isNull(this.languageService.getByLanguageKey(language, nftAttributeType.getClassName()))) {
                        var language_ = Language.builder()
                                .language(language.getCode())
                                .key(nftAttributeType.getClassName())
                                .value(com.billion.model.enums.Language.EN.getCode().equals(language.getCode()) ? file.getName() : EMPTY)
                                .build();
                        this.languageService.save(language_);
                        log.info("{}", language_);
                    }
                }

                for (var file_ : file.listFiles()) {
                    if (file_.isFile() && file_.getName().toLowerCase().endsWith(png)) {
                        var attribute = nftGroupId + ":" + nftAttributeType.getId() + ":" + file_.getName().substring(0, file_.getName().length() - png.length());

                        var nftAttributeMetaQueryWrapper = new QueryWrapper<NftAttributeMeta>();
                        nftAttributeMetaQueryWrapper.lambda().eq(NftAttributeMeta::getNftAttributeTypeId, nftAttributeType.getId());
                        nftAttributeMetaQueryWrapper.lambda().eq(NftAttributeMeta::getAttribute, attribute);
                        var nftAttributeMeta = this.nftAttributeMetaService.getOne(nftAttributeMetaQueryWrapper, false);
                        if (Objects.isNull(nftAttributeMeta)) {
                            nftAttributeMeta = NftAttributeMeta.builder()
                                    .nftAttributeTypeId(nftAttributeType.getId())
                                    .attribute(attribute)
                                    .value(EMPTY)
                                    .uri(file_.getAbsolutePath().substring(ContextService.getKikoNftImagePath().length()))
                                    .sort(EMPTY)
                                    .build();
                            this.nftAttributeMetaService.save(nftAttributeMeta);
                            log.info("{}", nftAttributeMeta);
                        }

                        for (com.billion.model.enums.Language language : com.billion.model.enums.Language.values()) {
                            if (Objects.isNull(this.languageService.getByLanguageKey(language, nftAttributeMeta.getAttribute()))) {
                                var language_ = Language.builder()
                                        .language(language.getCode())
                                        .key(nftAttributeMeta.getAttribute())
                                        .value(com.billion.model.enums.Language.EN.getCode().equals(language.getCode()) ? file_.getName().substring(0, file_.getName().length() - png.length()) : EMPTY)
                                        .build();
                                this.languageService.save(language_);
                                log.info("{}", language_);
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    public boolean exportNftAttributeTypeMeta(long nftGroupId) {
        var list = this.nftAttributeTypeService.getNftAttributeTypeMetaByNftGroupId(nftGroupId);
        var path = ContextService.getKikoPath() + "NftAttributeTypeMeta-NftGroupId-" + nftGroupId + ".xlsx";
        try {
            var result = Excel.writeMergeColumnExcel(path, list, "NftAttributeTypeMeta", "nftAttributeTypeId");
            log.info("result[{}] path[{}]", result, path);
            return result;
        } catch (Exception e) {
            log.error("{}", e);
            return false;
        }
    }

    public boolean generateNftMetaFile(long nftMetaId) {
        var nftMeta = this.nftMetaService.getById(nftMetaId);
        if (Objects.isNull(nftMeta)) {
            return false;
        }

        if (StringUtils.isNotEmpty(nftMeta.getFile())) {
            return true;
        }

        var nftAttributeValues = this.nftAttributeValueService.getByNftMetaId(nftMetaId);
        if (nftAttributeValues.isEmpty()) {
            return false;
        }

        var nftAttributeMetaQueryWrapper = new QueryWrapper<NftAttributeMeta>();
        nftAttributeMetaQueryWrapper.lambda().in(NftAttributeMeta::getId, nftAttributeValues.stream().map(e -> e.getNftAttributeMetaId()).collect(Collectors.toSet()));
        var nftAttributeMetas = this.nftAttributeMetaService.list(nftAttributeMetaQueryWrapper);
        if (nftAttributeValues.size() != nftAttributeMetas.size()) {
            return false;
        }

        var nftAttributeTypeIds = nftAttributeMetas.stream().map(e -> e.getNftAttributeTypeId()).collect(Collectors.toSet());

        var nftAttributeTypeQueryWrapper = new QueryWrapper<NftAttributeType>();
        nftAttributeTypeQueryWrapper.lambda().in(NftAttributeType::getId, nftAttributeTypeIds);
        nftAttributeTypeQueryWrapper.lambda().orderByAsc(NftAttributeType::getSort);
        var nftAttributeTypes = this.nftAttributeTypeService.list(nftAttributeTypeQueryWrapper);
        if (nftAttributeTypeIds.size() != nftAttributeTypes.size()) {
            return false;
        }

        var path = new ArrayList<String>(nftAttributeMetas.size());
        for (var nftAttributeType : nftAttributeTypes) {
            for (var nftAttributeMeta : nftAttributeMetas) {
                if (nftAttributeType.getId().longValue() == nftAttributeMeta.getNftAttributeTypeId().longValue()) {
                    path.add(ContextService.getKikoNftImagePath() + nftAttributeMeta.getUri());
                }
            }
        }

        var file = ContextService.getKikoNftImagePath() + nftMeta.getNftGroupId() + "/output/" + UUID.randomUUID().toString() + ".png";
        if (!Image.mergePicture(path, file)) {
            return false;
        }

        nftMeta.setFile(file.substring(ContextService.getKikoNftImagePath().length()));
        this.nftMetaService.updateById(nftMeta);
        return true;
    }

//1.规范图片命名
//2.导入图片到数据库
//3.导出nft_attribute_meta，人工设置分值
//4.导入设置分值的nft_attribute_meta
//
//
}