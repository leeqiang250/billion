package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.AccountCollectionData;
import com.aptos.request.v1.model.Resource;
import com.aptos.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.CollectionsMapper;
import com.billion.model.entity.Collections;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author liqiang
 */
@Slf4j
@Service
@SuppressWarnings({"rawtypes"})
public class CollectionsServiceImpl extends AbstractCacheService<CollectionsMapper, Collections> implements CollectionsService {

    @Override
    public void update(String account) {
        QueryWrapper<Collections> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Collections::getOwner, account);
        var list = this.getBaseMapper().selectList(wrapper);
        list.forEach(collections -> {
            if (StringUtils.isEmpty(collections.getCollectionDataHandle())
                    || StringUtils.isEmpty(collections.getTokenDataHandle())) {
                var accountCollectionData = AptosService.getAptosClient().requestAccountResource(account, Resource.Collections(), AccountCollectionData.class);
                collections.setCollectionDataHandle(accountCollectionData.getData().getCollectionData().getHandle());
                collections.setTokenDataHandle(accountCollectionData.getData().getTokenData().getHandle());

                getBaseMapper().updateById(collections);
            }
        });
        if (list.isEmpty()) {
            var accountCollectionData = AptosService.getAptosClient().requestAccountResource(account, Resource.Collections(), AccountCollectionData.class);

            var collections = Collections.builder()
                    .owner(account)
                    .collectionDataHandle(accountCollectionData.getData().getCollectionData().getHandle())
                    .tokenDataHandle(accountCollectionData.getData().getTokenData().getHandle())
                    .build();
            this.getBaseMapper().insert(collections);
        }
    }

}