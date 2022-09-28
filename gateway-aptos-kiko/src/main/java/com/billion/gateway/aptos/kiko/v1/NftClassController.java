package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.controller.IController;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftClass;
import com.billion.model.enums.Authenticate;
import com.billion.model.enums.AuthenticateType;
import com.billion.model.response.Response;
import com.billion.model.service.ICacheService;
import com.billion.service.aptos.kiko.NftClassService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;

import static com.billion.model.constant.v1.RequestPathNftV1.NFT_CLASS;

/**
 * @author jason
 */
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping(NFT_CLASS)
public class NftClassController implements IController<NftClass>{
    @Resource
    NftClassService nftClassService;

    @Override
    public ICacheService<NftClass> service() {
        return this.nftClassService;
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
    @Authenticate(AuthenticateType.FORBID)
    public Response cacheById(Context context, Serializable id) {
        return IController.super.cacheById(context, id);
    }

    @RequestMapping("getByNftId/{infoId}")
    public Response getNftClassByNftId(@RequestHeader Context context, @PathVariable String infoId) {
        return Response.success(nftClassService.getClassByInfoId(context, infoId));
    }

    @RequestMapping("test/{infoId}")
    public Response teset(@RequestHeader Context context, @PathVariable String infoId) {
        return Response.success(nftClassService.getClassForMint(infoId));
    }
}
