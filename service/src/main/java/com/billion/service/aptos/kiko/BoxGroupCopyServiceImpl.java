package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.TransactionPayload;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.BoxGroupCopyMapper;
import com.billion.model.dto.Context;
import com.billion.model.entity.BoxGroupCopy;
import com.billion.model.entity.Handle;
import com.billion.model.entity.Pair;
import com.billion.model.enums.Chain;
import com.billion.model.enums.TokenScene;
import com.billion.model.enums.TransactionStatus;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
import com.billion.service.aptos.ContextService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import static com.billion.model.constant.RequestPath.EMPTY;

/**
 * @author liqiang
 */
@Service
public class BoxGroupCopyServiceImpl extends AbstractCacheService<BoxGroupCopyMapper, BoxGroupCopy> implements BoxGroupCopyService {

    @Resource
    TokenService tokenService;

    @Resource
    PairService pairService;

    @PostConstruct
    @Override
    public boolean initialize() {
        if (!tokenService.initialize()) {
            return false;
        }

        QueryWrapper<BoxGroupCopy> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BoxGroupCopy::getChain, Chain.APTOS.getCode());
        wrapper.lambda().eq(BoxGroupCopy::getEnabled, Boolean.TRUE);
        wrapper.lambda().eq(BoxGroupCopy::getTransactionStatus, TransactionStatus.STATUS_1_READY.getCode());
        var boxGroups = super.list(wrapper);

        for (int i = 0; i < boxGroups.size(); i++) {
            var boxGroup = boxGroups.get(i);

            var askToken = tokenService.getById(boxGroup.getAskToken());
            var bidToken = tokenService.getById(boxGroup.getBidToken());

            if (Objects.isNull(askToken)
                    || Objects.isNull(bidToken)
                    || TransactionStatus.STATUS_3_SUCCESS != askToken.getTransactionStatus_()
                    || TransactionStatus.STATUS_3_SUCCESS != bidToken.getTransactionStatus_()
            ) {
                boxGroup.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
                super.updateById(boxGroup);

                return false;
            }

            com.aptos.request.v1.model.Resource askTokenResource = com.aptos.request.v1.model.Resource.builder()
                    .moduleAddress(askToken.getModuleAddress())
                    .moduleName(askToken.getModuleName())
                    .resourceName(askToken.getStructName())
                    .build();

            com.aptos.request.v1.model.Resource bidTokenResource = com.aptos.request.v1.model.Resource.builder()
                    .moduleAddress(bidToken.getModuleAddress())
                    .moduleName(bidToken.getModuleName())
                    .resourceName(bidToken.getStructName())
                    .build();

            TransactionPayload transactionPayload = TransactionPayload.builder()
                    .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                    .function(ContextService.getMarketer() + "::box_primary_market::add_box_bid")
                    .arguments(List.of(
                            boxGroup.getAmount(),
                            boxGroup.getTs(),
                            boxGroup.getPrice()
                    ))
                    .typeArguments(List.of(askTokenResource.resourceTag(), bidTokenResource.resourceTag()))
                    .build();

            var response = AptosService.getAptosClient().requestSubmitTransaction(
                    ContextService.getMarketer(),
                    transactionPayload);
            if (response.isValid()) {
                boxGroup.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
                super.updateById(boxGroup);

                return false;
            }

            if (!AptosService.checkTransaction(response.getData().getHash())) {
                boxGroup.setTransactionHash(Objects.isNull(response.getData().getHash()) ? EMPTY : response.getData().getHash());
                boxGroup.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
                super.updateById(boxGroup);

                return false;
            }

            boxGroup.setTransactionHash(response.getData().getHash());
            boxGroup.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);

            super.updateById(boxGroup);
        }

        return this.initializMarket();
    }

    @Override
    public boolean initializMarket() {
        Context context = Context.builder()
                .chain(Chain.APTOS.getCode())
                .build();

        QueryWrapper<BoxGroupCopy> boxGroupQueryWrapper = new QueryWrapper<>();
        boxGroupQueryWrapper.lambda().eq(BoxGroupCopy::getChain, Chain.APTOS.getCode());
        boxGroupQueryWrapper.lambda().eq(BoxGroupCopy::getEnabled, Boolean.TRUE);
        boxGroupQueryWrapper.lambda().eq(BoxGroupCopy::getTransactionStatus, TransactionStatus.STATUS_3_SUCCESS.getCode());
        var boxGroups = super.list(boxGroupQueryWrapper);

        var tokens = tokenService.getByScene(context, TokenScene.MARKET.getCode());

        for (int i = 0; i < boxGroups.size(); i++) {
            var askToken = tokenService.getById(boxGroups.get(i).getAskToken());

            for (int j = 0; j < tokens.size(); j++) {
                var bidToken = tokens.get(j);

                QueryWrapper<Pair> pairQueryWrapper = new QueryWrapper<>();
                pairQueryWrapper.lambda().eq(Pair::getAskToken, askToken.getId());
                pairQueryWrapper.lambda().eq(Pair::getBidToken, bidToken.getId());
                var pair = pairService.getOne(pairQueryWrapper, false);
                if (Objects.isNull(pair)) {
                    com.aptos.request.v1.model.Resource askTokenResource = com.aptos.request.v1.model.Resource.builder()
                            .moduleAddress(askToken.getModuleAddress())
                            .moduleName(askToken.getModuleName())
                            .resourceName(askToken.getStructName())
                            .build();

                    com.aptos.request.v1.model.Resource bidTokenResource = com.aptos.request.v1.model.Resource.builder()
                            .moduleAddress(bidToken.getModuleAddress())
                            .moduleName(bidToken.getModuleName())
                            .resourceName(bidToken.getStructName())
                            .build();

                    TransactionPayload transactionPayload = TransactionPayload.builder()
                            .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
                            .function(ContextService.getMarketer() + "::box_secondary_market::box_init")
                            .arguments(List.of())
                            .typeArguments(List.of(askTokenResource.resourceTag(), bidTokenResource.resourceTag()))
                            .build();

                    var response = AptosService.getAptosClient().requestSubmitTransaction(
                            ContextService.getMarketer(),
                            transactionPayload);
                    if (response.isValid()) {
                        return false;
                    }

                    if (!AptosService.checkTransaction(response.getData().getHash())) {
                        return false;
                    }

                    pair = Pair.builder()
                            .askToken(askToken.getId())
                            .bidToken(bidToken.getId())
                            .build();

                    pairService.save(pair);
                }

            }
        }

        return true;
    }

}