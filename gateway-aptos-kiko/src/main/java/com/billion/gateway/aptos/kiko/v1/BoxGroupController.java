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

    @GetMapping("getMyBox/{account}")
    public Response getMyBox(@RequestHeader Context context, @PathVariable String account) {
        return Response.success(boxGroupService.getMyBox(context, account));
    }

    @GetMapping(value = {"getSaleList/{pageStart}/{pageLimit}", "getSaleList"})
    public Response getSaleList(@RequestHeader Context context, @PathVariable(required = false) Integer pageStart, @PathVariable(required = false) Integer pageLimit) {
        return Response.success(boxGroupService.getSaleList(context, pageStart, pageLimit));
    }

}
