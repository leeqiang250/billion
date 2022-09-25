package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.AccountCollectionData;
import com.aptos.request.v1.model.Resource;
import com.aptos.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.HandleMapper;
import com.billion.model.entity.Handle;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author liqiang
 */
@Service
public class HandleServiceImpl extends AbstractCacheService<HandleMapper, Handle> implements HandleService {

    @Override
    public boolean update(String account) {
        var accountCollectionData = AptosService.getAptosClient().requestAccountResource(account, Resource.Collections(), AccountCollectionData.class);

        QueryWrapper<Handle> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Handle::getOwner, account);
        var handle = this.getBaseMapper().selectOne(wrapper);
        if (Objects.isNull(handle)
                || StringUtils.isEmpty(handle.getCollectionsCollectionDataHandle())
                || StringUtils.isEmpty(handle.getCollectionsTokenDataHandle())) {


            handle = Handle.builder()
                    .owner(account)
                    .collectionsCollectionDataHandle(accountCollectionData.getData().getCollectionData().getHandle())
                    .collectionsTokenDataHandle(accountCollectionData.getData().getTokenData().getHandle())
                    .build();

            this.getBaseMapper().insert(handle);
        } else {
            handle.setCollectionsCollectionDataHandle(accountCollectionData.getData().getCollectionData().getHandle());
            handle.setCollectionsTokenDataHandle(accountCollectionData.getData().getTokenData().getHandle());

            this.getBaseMapper().updateById(handle);
        }

        return true;
    }

}