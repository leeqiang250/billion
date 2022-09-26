package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.AccountCollectionData;
import com.aptos.request.v1.model.AccountTokenStore;
import com.aptos.request.v1.model.Resource;
import com.aptos.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.HandleMapper;
import com.billion.model.entity.Handle;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.billion.model.constant.RequestPath.EMPTY;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class HandleServiceImpl extends AbstractCacheService<HandleMapper, Handle> implements HandleService {

    @Override
    public void update(String account) {
        QueryWrapper<Handle> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Handle::getOwner, account);
        var handle = super.getOne(wrapper, false);

        try {
            var accountCollectionData = AptosService.getAptosClient().requestAccountResource(account, Resource.Collections(), AccountCollectionData.class);
            if (Objects.nonNull(accountCollectionData)) {
                if (Objects.isNull(handle)) {
                    handle = Handle.builder()
                            .owner(account)
                            .tokenStoreTokensHandle(EMPTY)
                            .build();
                }

                handle.setCollectionsCollectionDataHandle(accountCollectionData.getData().getCollectionData().getHandle());
                handle.setCollectionsTokenDataHandle(accountCollectionData.getData().getTokenData().getHandle());

                super.saveOrUpdate(handle);
            }
        } catch (Exception e) {
            log.error("{}", e.toString());
        }

        handle = super.getOne(wrapper, false);
        try {
            var accountTokenStore = AptosService.getAptosClient().requestAccountResource(account, Resource.TokenStore(), AccountTokenStore.class);
            if (Objects.nonNull(accountTokenStore)) {
                if (Objects.isNull(handle)) {
                    handle = Handle.builder()
                            .owner(account)
                            .collectionsCollectionDataHandle(EMPTY)
                            .collectionsTokenDataHandle(EMPTY)
                            .build();
                }
                handle.setTokenStoreTokensHandle(accountTokenStore.getData().getTokens().getHandle());

                super.saveOrUpdate(handle);
            }
        } catch (Exception e) {
            log.error("{}", e.toString());
        }
    }

    @Override
    public Handle getByAccount(String account) {
        QueryWrapper<Handle> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Handle::getOwner, account);
        var handle = super.getOne(wrapper, false);
        if (Objects.isNull(handle)
                || StringUtils.isEmpty(handle.getCollectionsCollectionDataHandle())
                || StringUtils.isEmpty(handle.getCollectionsTokenDataHandle())
                || StringUtils.isEmpty(handle.getTokenStoreTokensHandle())
        ) {
            this.update(account);
            handle = super.getOne(wrapper, false);
        }

        return handle;
    }

}