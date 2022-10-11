package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.controller.IController;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftInfo;
import com.billion.model.enums.Authenticate;
import com.billion.model.enums.AuthenticateType;
import com.billion.model.response.Response;
import com.billion.model.service.ICacheService;
import com.billion.service.aptos.kiko.NftInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.billion.model.constant.v1.RequestPathNftV1.NFT_INFO;

/**
 * @author liqiang
 */
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping(NFT_INFO)
public class NftInfoController implements IController<NftInfo> {

    @Resource
    NftInfoService nftInfoService;

    @Override
    public ICacheService<NftInfo> service() {
        return this.nftInfoService;
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

    @GetMapping("/getListByGroup/{type}/{groupId}")
    public Response getListByGroup(@RequestHeader Context context, @PathVariable String type, @PathVariable String groupId) {
        return Response.success(nftInfoService.getListByGroup(context, type, groupId));
    }

}