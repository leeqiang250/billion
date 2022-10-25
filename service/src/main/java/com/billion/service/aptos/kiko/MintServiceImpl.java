package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.model.entity.BoxGroup;
import com.billion.model.entity.Language;
import com.billion.model.entity.NftAttributeMeta;
import com.billion.model.entity.NftAttributeType;
import com.billion.model.enums.Chain;
import com.billion.model.enums.TransactionStatus;
import com.billion.service.aptos.ContextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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


    public boolean fsdfd(long nftGroupId) {
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
}