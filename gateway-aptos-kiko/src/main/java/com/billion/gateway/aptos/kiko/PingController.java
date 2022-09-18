package com.billion.gateway.aptos.kiko;

import com.billion.model.response.Response;
import com.billion.service.aptos.ContextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.billion.model.constant.RequestPath.SLASH;

/**
 * @author liqiang
 */
@Slf4j
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping({SLASH})
public class PingController {

    @RequestMapping({SLASH})
    public Response ping() {
        return Response.success(ContextService.getApplicationName() + ":" + ContextService.getEnv());
    }

}