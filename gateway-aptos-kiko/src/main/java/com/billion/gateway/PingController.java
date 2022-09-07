package com.billion.gateway;

import com.billion.model.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liqiang
 */
@RestController
@RequestMapping({"", "/", "/ping"})
public class PingController {

    @GetMapping(path = {"", "/", "/success"})
    public Response success() {
        return Response.success();
    }

    @GetMapping("/failure")
    public Response failure() {
        return Response.failure();
    }

}