package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.controller.IController;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftAttribute;
import com.billion.model.response.Response;
import com.billion.model.service.ICacheService;
import com.billion.service.aptos.kiko.NftAttributeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.Serializable;

import static com.billion.model.constant.RequestPathConstant.V1_NFT_ATTRIBUTE;

/**
 * @author liqiang
 */
@RestController
@RequestMapping(V1_NFT_ATTRIBUTE)
@SuppressWarnings({"rawtypes"})
public class NftAttributeController implements IController<NftAttribute> {

    @Resource
    NftAttributeService nftAttributeService;

    @Override
    public ICacheService<NftAttribute> service() {
        return this.nftAttributeService;
    }

    @Override
    public Response cacheGet(Context context) {
        return Response.failure();
    }

    @Override
    public Response cacheGetById(Context context, Serializable id) {
        return Response.success(this.nftAttributeService.getByGroupId(context, id.toString()));
    }

}