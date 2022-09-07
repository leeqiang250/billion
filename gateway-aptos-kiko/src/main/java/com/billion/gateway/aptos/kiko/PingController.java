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

    //    @Value("${spring.application.name}")
    private String applicationName;

//    @Value("${demo.param1}")
    private String param;

    @GetMapping(path = {"", "/", "/success"})
    public Response success() {
        return Response.success(applicationName);
    }

    @GetMapping("/failure")
    public Response failure() {
        return Response.failure(applicationName);
    }

}