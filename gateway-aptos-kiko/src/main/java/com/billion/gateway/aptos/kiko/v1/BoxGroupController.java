package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.dto.Context;
import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.BoxGroupService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author liqiang
 */
@RestController
@RequestMapping("/box-group")
public class BoxGroupController {
    @Resource
    BoxGroupService boxGroupService;

    @GetMapping("getMyBox/{account}")
    public Response getMyBox(@RequestHeader Context context, @PathVariable String account) {
        return Response.success(boxGroupService.getMyBox(context, account));
    }

    @GetMapping("getSaleList")
    public Response getSaleList(@RequestHeader Context context) {
        return Response.success(boxGroupService.getSaleList(context));
    }

}
