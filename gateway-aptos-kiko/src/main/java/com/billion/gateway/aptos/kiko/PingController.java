package com.billion.gateway.aptos.kiko;

import com.billion.model.response.Response;
import com.billion.service.aptos.AptosService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author liqiang
 */
@RestController
@RequestMapping({"/", "/ping"})
public class PingController {

    @Value("${spring.application.name}")
    String applicationName;

    @Value("${spring.profiles.active}")
    String profilesActive;

    @Resource
    AptosService aptosService;

    @GetMapping(path = {"/", "/failure", "/success"})
    public Response ping() {
        return Response.success(applicationName + ":" + profilesActive);
    }

}