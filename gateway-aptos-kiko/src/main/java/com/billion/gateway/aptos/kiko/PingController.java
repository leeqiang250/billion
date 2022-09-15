package com.billion.gateway.aptos.kiko;

import com.billion.model.response.Response;
import com.billion.service.aptos.AptosService;
import com.billion.service.aptos.ContextService;
import com.billion.service.aptos.kiko.IImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author liqiang
 */
@Slf4j
@RestController
@RequestMapping({"/", "/ping"})
public class PingController {

    @Resource
    ContextService contextService;

    @Resource
    AptosService aptosService;

    @Resource
    IImageService imageService;

    @GetMapping(path = {"", "/", "/failure", "/success"})
    public Response ping() {
        return Response.success(this.contextService.getApplicationName() + ":" + this.contextService.getEnv());
    }

}