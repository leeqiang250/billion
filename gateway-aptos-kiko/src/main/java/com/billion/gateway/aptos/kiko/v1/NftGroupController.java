package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.controller.IController;
import com.billion.model.service.ICacheService;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftGroup;
import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.NftGroupService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.billion.model.constant.v1.RequestPathNftV1.NFT_GROUP;

/**
 * @author liqiang
 */
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping(NFT_GROUP)
public class NftGroupController implements IController<NftGroup> {

    @Resource
    NftGroupService nftGroupService;

    @Override
    public ICacheService<NftGroup> service() {
        return this.nftGroupService;
    }

    @RequestMapping("/{meta}/{body}")
    public Response cacheGetById(@RequestHeader Context context, @PathVariable String meta, @PathVariable String body) {
        return Response.success(this.nftGroupService.getByMetaBody(context, meta, body));
    }

}