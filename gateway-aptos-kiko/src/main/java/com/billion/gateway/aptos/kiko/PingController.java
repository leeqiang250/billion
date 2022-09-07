package com.billion.gateway.aptos.kiko;

import com.billion.model.response.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liqiang
 */
@RestController
@RequestMapping({"", "/", "/ping"})
public class PingController {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.profiles.active}")
    private String profilesActive;

    @GetMapping(path = {"", "/", "/failure", "/success"})
    public Response ping() {
        return Response.success(applicationName + ":" + profilesActive);
    }

}