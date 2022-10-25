package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.dto.Context;
import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.BoxGroupService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.billion.model.constant.v1.RequestPathNftV1.BOX_GROUP;

/**
 * @author liqiang
 */
@RestController
@RequestMapping(BOX_GROUP)
public class BoxGroupController {
    @Resource
    BoxGroupService boxGroupService;


    @GetMapping("getBoxGroupById/{boxId}")
    public Response getBoxGroupById(@RequestHeader Context context, @PathVariable String boxId) {
        return Response.success(boxGroupService.getBoxGroupById(context, boxId));
    }

    @GetMapping("getMyBox/{account}/{saleState}")
    public Response getMyBox(@RequestHeader Context context, @PathVariable String account, @PathVariable String saleState) {
        return Response.success(boxGroupService.getMyBox(context, account, saleState));
    }

    @GetMapping({"getBoxById/{groupId}/{account}/{saleState}/{orderId}", "getBoxById/{groupId}/{account}/{saleState}"})
    public Response getBoxById(@RequestHeader Context context, @PathVariable String groupId, @PathVariable String account, @PathVariable String saleState, @PathVariable(required = false) String orderId) {
        return Response.success(boxGroupService.getBoxById(context, groupId, account, saleState, orderId));
    }

    @GetMapping(value = {"getSaleList/{pageStart}/{pageLimit}", "getSaleList"})
    public Response getSaleList(@RequestHeader Context context, @PathVariable(required = false) Integer pageStart, @PathVariable(required = false) Integer pageLimit) {
        return Response.success(boxGroupService.getSaleList(context, pageStart, pageLimit));
    }


}
