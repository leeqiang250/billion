package com.billion.gateway.aptos.kiko;

import com.billion.model.response.Response;
import com.billion.service.aptos.ContextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.billion.model.constant.RequestPathConstant.*;

/**
 * @author liqiang
 */
@Slf4j
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping({SLASH, PING})
public class PingController {

    @RequestMapping({EMPTY, SLASH})
    public Response ping() {
        return Response.success(ContextService.getApplicationName() + ":" + ContextService.getEnv());
    }

}