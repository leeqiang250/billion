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

import java.util.stream.Collectors;

import static com.billion.model.constant.RequestPathConstant.*;

/**
 * @author liqiang
 */
@RestController
@RequestMapping(V1_NFT_GROUP)
public class NftGroupController implements IController<NftGroup> {

    @Resource
    NftGroupService nftGroupService;

    @Override
    public ICacheService<NftGroup> service() {
        return this.nftGroupService;
    }

    @Override
    public Response get(Context context) {
        return Response.success(this.nftGroupService.getAllById(context).values().stream().collect(Collectors.toList()));
    }

    @RequestMapping("/{meta}/{body}")
    public Response get(@RequestHeader Context context, @PathVariable String meta, @PathVariable String body) {
        return Response.success(this.nftGroupService.getByMetaBody(context, meta, body));
    }

}