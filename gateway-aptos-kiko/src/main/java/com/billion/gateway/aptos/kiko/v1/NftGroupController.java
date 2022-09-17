package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.dto.Context;
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
public class NftGroupController {

    @Resource
    NftGroupService nftGroupService;

    @RequestMapping({EMPTY, SLASH})
    public Response get(@RequestHeader Context context) {
        return Response.success(this.nftGroupService.getAllById(context).values().stream().collect(Collectors.toList()));
    }

    @RequestMapping("/{id}")
    public Response get(@PathVariable Long id, @RequestHeader Context context) {
        return Response.success(this.nftGroupService.getById(id, context));
    }

    @RequestMapping("/{meta}/{body}")
    public Response get(@PathVariable String meta, @PathVariable String body, @RequestHeader Context context) {
        return Response.success(this.nftGroupService.getByMetaBody(meta, body, context));
    }

}
