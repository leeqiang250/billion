package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.controller.IController;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftInfo;
import com.billion.model.response.Response;
import com.billion.model.service.ICacheService;
import com.billion.service.aptos.kiko.NftInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.io.Serializable;

import static com.billion.model.constant.RequestPathConstant.V1_NFT_INFO;

/**
 * @author liqiang
 */
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping(V1_NFT_INFO)
public class NftInfoController implements IController<NftInfo> {

    @Resource
    NftInfoService nftInfoService;

    @Override
    public ICacheService<NftInfo> service() {
        return this.nftInfoService;
    }

    @Override
    public Response cacheGet(Context context) {
        return null;
    }

    @Override
    public Response cacheGetById(Context context, Serializable id) {
        return null;
    }

}
