package com.billion.gateway.aptos.kiko.v1;

import com.aptos.request.v1.model.Resource;
import com.billion.model.dto.Context;
import com.billion.model.response.Response;
import com.billion.service.aptos.AptosService;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.billion.model.constant.v1.RequestPathAptosResourceV1.APTOS_RESOURCE;

/**
 * 查询Aptos链上资源接口
 * @author Jason
 */
@RestController
@RequestMapping(APTOS_RESOURCE)
public class AptosResourceController {

    @GetMapping("getBalance/{account}/{coinType}")
    public Response getBalance(@RequestHeader Context context, @PathVariable String account, @PathVariable String coinType) {
        if (!validcoin(coinType)) {
            return Response.failure("invalid coninType.");
        }

        Resource askTokenResource = Resource.builder()
                .moduleAddress(coinType.split("::")[0])
                .moduleName(coinType.split("::")[1])
                .resourceName(coinType.split("::")[2])
                .build();
        var result = AptosService.getAptosClient().requestCoinStore(account, askTokenResource);
        return Response.success(result.getData());
    }

    private boolean validcoin(String coinType) {
        if (Objects.isNull(coinType)) {
            return false;
        }
        String[] coinInfo = coinType.split("::");
        if (coinInfo.length != 3) {
            return false;
        }

        return true;
    }
}
