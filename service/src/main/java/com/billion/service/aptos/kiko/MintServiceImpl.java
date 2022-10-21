package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.model.entity.BoxGroup;
import com.billion.model.enums.Chain;
import com.billion.model.enums.TransactionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

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

}