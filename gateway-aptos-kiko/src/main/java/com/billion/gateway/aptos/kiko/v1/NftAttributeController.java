package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.controller.IController;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftAttribute;
import com.billion.model.enums.Authenticate;
import com.billion.model.enums.AuthenticateType;
import com.billion.model.response.Response;
import com.billion.model.service.ICacheService;
import com.billion.service.aptos.kiko.NftAttributeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.Serializable;

import static com.billion.model.constant.v1.RequestPathNftV1.NFT_ATTRIBUTE;

/**
 * @author liqiang
 */
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping(NFT_ATTRIBUTE)
public class NftAttributeController implements IController<NftAttribute> {

    @Resource
    NftAttributeService nftAttributeService;

    @Override
    public ICacheService<NftAttribute> service() {
        return this.nftAttributeService;
    }

    @Override
    @Authenticate(AuthenticateType.FORBID)
    public Response cacheList(Context context) {
        return IController.super.cacheList(context);
    }

    @Override
    @Authenticate(AuthenticateType.FORBID)
    public Response cacheMap(Context context) {
        return IController.super.cacheMap(context);
    }

    @Override
    public Response cacheById(Context context, Serializable id) {
        return Response.success(this.nftAttributeService.getByGroupId(context, id.toString()));
    }

}