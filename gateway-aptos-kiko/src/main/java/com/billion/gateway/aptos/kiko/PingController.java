package com.billion.gateway.aptos.kiko;

import com.billion.model.response.Response;
import com.billion.service.aptos.AptosService;
import com.billion.service.aptos.ContextService;
import com.billion.service.aptos.kiko.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.billion.model.constant.RequestPathConstant.PING;
import static com.billion.model.constant.RequestPathConstant.SLASH;

/**
 * @author liqiang
 */
@Slf4j
@RestController
@RequestMapping({SLASH, PING})
public class PingController {

    @Resource
    ContextService contextService;

    @Resource
    AptosService aptosService;

    @Resource
    ImageService imageService;

    @RequestMapping(path = {SLASH, "/failure", "/success"})
    public Response ping() {
        return Response.success(ContextService.getApplicationName() + ":" + ContextService.getEnv());
    }

}