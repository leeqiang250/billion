package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.controller.IController;
import com.billion.model.dto.Context;
import com.billion.model.entity.Market;
import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.MarketService;
import com.billion.service.aptos.kiko.OperationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.billion.model.constant.v1.RequestPathMarketV1.MARKET;


/**
 * @author Jason
 */
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping(MARKET)
public class MarketController  implements IController<Market> {
    @Resource
    MarketService marketService;

    @Resource
    OperationService operationService;

    @GetMapping("getMarketList/{condition}/{order}/{orderType}/{pageStart}/{pageLimit}")
    public Response getMarketList(@RequestHeader Context context, @PathVariable String condition, @PathVariable String order,
                                  @PathVariable String orderType, @PathVariable Integer pageStart, @PathVariable Integer pageLimit) {
        return Response.success(marketService.getMarketList(context, condition, order, orderType, pageStart, pageLimit));
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
