package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.TransactionPayload;
import com.aptos.utils.Hex;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.BoxGroupMapper;
import com.billion.model.dto.Context;
import com.billion.model.entity.BoxGroup;
import com.billion.model.enums.CacheTsType;
import com.billion.model.enums.TransactionStatus;
import com.billion.model.response.Response;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jason
 */
@Service
@Slf4j
public class BoxGroupServiceImpl extends AbstractCacheService<BoxGroupMapper, BoxGroup> implements BoxGroupService {
    @Resource
    LanguageService languageService;
//    @Override
//    public List<BoxGroup> cacheList(Context context) {
//
//    }


    @Override
    public Map cacheMap(Context context) {
        String key = this.cacheMapKey("ids::" + context.getChain());

        Map map = this.getRedisTemplate().opsForHash().entries(key);
        if (!map.isEmpty()) {
            return map;
        }

        QueryWrapper<BoxGroup> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BoxGroup::getEnabled, Boolean.TRUE);
        List<BoxGroup> list = super.list(wrapper);

        changeLanguage(context, list);

        map = list.stream().collect(Collectors.toMap(e -> e.getId().toString(), (e) -> e));

        this.getRedisTemplate().opsForHash().putAll(key, map);
        this.getRedisTemplate().expire(key, this.cacheSecond(CacheTsType.MIDDLE));

        return map;
    }

    @Override
    public List<BoxGroup> cacheList(Context context) {
        Map map = this.cacheMap(context);
        List<BoxGroup> list =  new ArrayList<>(map.values());
        return list;
    }

    private void changeLanguage(Context context, List<BoxGroup> list) {
        Set setDisplayName = list.stream().map(e -> e.getDisplayName()).collect(Collectors.toSet());
        Set setDescription = list.stream().map(e -> e.getDescription()).collect(Collectors.toSet());
        Set setRule = list.stream().map(e -> e.getRule()).collect(Collectors.toSet());

        Map mapDisplayName = languageService.getByKeys(context, setDisplayName);
        Map mapDescription = languageService.getByKeys(context, setDescription);
        Map mapRule = languageService.getByKeys(context, setRule);

        list.forEach(e -> {
            e.setDisplayName(mapDisplayName.get(e.getDisplayName()).toString());
            e.setDescription(mapDescription.get(e.getDescription()).toString());
            e.setRule(mapRule.get(e.getRule()).toString());
        });
    }

    @Override
    public void initBox(Context context, String groupId) {
        //初始化
        //addbox
        //addpay

        BoxGroup boxGroup = this.cacheById(context, groupId);
        if (!Objects.nonNull(boxGroup)) {
            log.error("box group is null!");
        }

//        TransactionPayload transactionPayload = TransactionPayload.builder()
//                .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
//                .function("0x3::token::create_collection_script")
//                .arguments(List.of(
//                        Hex.encode(displayName),
//                        Hex.encode(description),
//                        Hex.encode(uri),
//                        nftGroup.getTotalSupply(),//TODO
//                        List.of(true, true, true)//TODO
//                ))
//                .typeArguments(List.of())
//                .build();
//
//        var response = AptosService.getAptosClient().requestSubmitTransaction(
//                nftGroup.getOwner(),
//                transactionPayload);
//        if (response.isValid()) {
//            return false;
//        }
//
//        if (!AptosService.checkTransaction(response.getData().getHash())) {
//            nftGroup.setTransactionStatus_(TransactionStatus.STATUS_4_FAILURE);
//            return false;
//        }
//
//        nftGroup.setTransactionHash(response.getData().getHash());
//        nftGroup.setTransactionStatus_(TransactionStatus.STATUS_3_SUCCESS);
//
//        super.updateById(nftGroup);

    }

    @Override
    public void addPay(String groupId) {

    }

    //售卖中的
    //售罄的
    //即将开售的

//    /**
//     *
//     * @param context
//     * @param type
//     * @return
//     */
//    @Override
//    public Response getList(Context context, String type) {
//       List<BoxGroup> boxList = this.cacheList(context);
//       boxList.forEach(e -> {
//           switch (type) {
//               case "comingSoon":
//                   if (LocalDateTime.now().isBefore(e.getSaleTime())) {
//
//                   }
//               case "onSale":
//                   if (LocalDateTime.now().isAfter(e.getSaleTime()) && LocalDateTime.now().isBefore(e.getEndTime())) {
//
//                   }
//               case "sellOut":
//                   if (LocalDateTime.now().isAfter(e.getEndTime())) {
//
//                   }
//
//           }
//       });
//
//    }
}
