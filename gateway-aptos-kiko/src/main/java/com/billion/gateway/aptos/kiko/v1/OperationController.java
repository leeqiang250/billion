package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.controller.IController;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftMeta;
import com.billion.model.entity.Operation;
import com.billion.model.enums.Authenticate;
import com.billion.model.enums.AuthenticateType;
import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.OperationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.billion.model.constant.v1.RequestPathOperationV1.OPERATION;


/**
 * @author Jason
 */
@RestController
@RequestMapping(OPERATION)
public class OperationController implements IController<Operation> {

    @Resource
    OperationService operationService;

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

    @GetMapping("/getList/{tokenId}")
    public Response getList(@RequestHeader Context context, @PathVariable String tokenId) {
        return Response.success(operationService.getListById(context, tokenId));
    }

    @GetMapping("/getSaleRecord/{account}")
    public Response getSaleRecord(@RequestHeader Context context, @PathVariable String account) {
        return Response.success(operationService.getSaleRecord(context, account));
    }

    @GetMapping("/getBuyRecord/{account}")
    public Response getBuyRecord(@RequestHeader Context context, @PathVariable String account) {
        return Response.success(operationService.getBuyRecord(context, account));
    }

}
