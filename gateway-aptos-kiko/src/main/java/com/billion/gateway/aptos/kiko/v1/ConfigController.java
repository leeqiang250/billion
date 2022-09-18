package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.controller.IController;
import com.billion.model.service.ICacheService;
import com.billion.model.dto.Context;
import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.io.Serializable;

import static com.billion.model.constant.RequestPathConstant.*;

/**
 * @author liqiang
 */
@Slf4j
@RestController
@RequestMapping(V1_CONFIG)
public class ConfigController implements IController<Object> {

    @Resource
    ConfigService configService;

    @Override
    public ICacheService<Object> service() {
        return null;
    }

    @Override
    public Response get(Context context) {
        log.info("[request context] context:[{}]", context.toString());
        return Response.success(configService.get(context));
    }

    @Override
    public Response get(Context context, Serializable id) {
        return Response.failure();
    }


}