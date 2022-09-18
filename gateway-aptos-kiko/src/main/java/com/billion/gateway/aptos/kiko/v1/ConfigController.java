package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.controller.IController;
import com.billion.model.enums.Authenticate;
import com.billion.model.enums.AuthenticateType;
import com.billion.model.model.IModel;
import com.billion.model.dto.Context;
import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.ConfigService;
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

    @Override
    public Response cacheMap(Context context) {
        log.info("[request context] {}", context.toString());
        return Response.success(configService.get(context));
    }

    @Override
    public Response cacheList(Context context) {
        log.info("[request context] {}", context.toString());
        return Response.success(configService.get(context));
    }

}