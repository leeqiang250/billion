package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.dto.Context;
import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.billion.model.constant.RequestPathConstant.*;

/**
 * @author liqiang
 */
@Slf4j
@RestController
@RequestMapping(V1_CONFIG)
public class ConfigController {

    @Resource
    ConfigService configService;

    @RequestMapping({EMPTY, SLASH})
    public Response get(@RequestHeader Context context) {
        log.info("[request context] context:[{}]", context.toString());
        return Response.success(configService.get(context));
    }

}