package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.controller.IController;
import com.billion.model.dto.Context;
import com.billion.model.entity.Market;
import com.billion.model.entity.NftAttribute;
import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.MarketService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("getMarketList")
    public Response getMarketList(@RequestHeader Context context) {
        return Response.success(marketService.getMarketList(context));
    }
}
