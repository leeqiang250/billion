package com.billion.gateway.aptos.kiko.v1;

import com.aptos.request.v1.model.Resource;
import com.billion.model.dto.Context;
import com.billion.model.response.Response;
import com.billion.service.aptos.AptosService;
import org.springframework.web.bind.annotation.*;

import static com.billion.model.constant.v1.RequestPathAptosResourceV1.APTOS_RESOURCE;

/**
 * 查询Aptos链上资源接口
 *
 * @author Jason
 */
@RestController
@RequestMapping(APTOS_RESOURCE)
public class ResourceController {

    @GetMapping("getBalance/{account}/{coinType}")
    public Response getBalance(@RequestHeader Context context, @PathVariable String account, @PathVariable String coinType) {
        var resource = Resource.ofStruct(coinType);
        try {
            return Response.success(AptosService.getAptosClient().requestCoinStore(account, resource).getData());
        } catch (Exception e) {
            return Response.success("0");
        }
    }

}