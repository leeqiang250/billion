package com.billion.gateway.aptos.kiko.v1;

import com.aptos.request.v1.model.Transaction;
import com.billion.model.controller.IController;
import com.billion.model.entity.Token;
import com.billion.model.enums.Authenticate;
import com.billion.model.enums.AuthenticateType;
import com.billion.model.model.IModel;
import com.billion.model.dto.Context;
import com.billion.model.response.Response;
import com.billion.service.aptos.AptosService;
import com.billion.service.aptos.kiko.ConfigService;
import com.billion.service.aptos.kiko.NftGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.billion.model.constant.v1.RequestPathConfigV1.CONFIG;

/**
 * @author liqiang
 */
@Slf4j
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping(CONFIG)
public class ConfigController implements IController<IModel> {

    @Resource
    ConfigService configService;

    @Resource
    NftGroupService nftGroupService;

    @Override
    public Response cacheMap(Context context) {
        {
            Transaction transaction = nftGroupService.transferApt(
                    "0xe89e92f0ea0ccb394fa3cb10a72ad866c4ad786956898fe7164731aa348ec1c5",
                    "0x1ff00114f046e27033b9bfdcd217fcf50023b576cbd3baaafe9674961632a5bd",
                    "1");

            AptosService.checkTransaction(transaction.getHash());
        }
        {
            Token token = Token.builder()
                    .moduleAddress("0xf7e09293bfc8a0c70a4bf9b6fecc4527da518dc4d8a60a84c293de6854dae0d8")
                    .moduleName("kingdom_token_v1")
                    .resourceName("KingdomTokenV1")
                    .initializeFunction("0xf7e09293bfc8a0c70a4bf9b6fecc4527da518dc4d8a60a84c293de6854dae0d8::token_v1::initialize")
                    .name("name")
                    .symbol("symbol")
                    .build();

            Transaction transaction = nftGroupService.initToken(token);
            AptosService.checkTransaction(transaction.getHash());
        }
        log.info("[request context] {}", context.toString());
        return Response.success(configService.get(context));
    }

    @Override
    public Response cacheList(Context context) {
        log.info("[request context] {}", context.toString());
        return Response.success(configService.get(context));
    }

}