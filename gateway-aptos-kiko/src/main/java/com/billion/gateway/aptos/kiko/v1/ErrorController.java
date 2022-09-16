package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.response.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.billion.model.constant.RequestPathConstant.*;

/**
 * @author liqiang
 */
@RestController
@RequestMapping({V1_ERROR})
public class ErrorController {

    @RequestMapping({EMPTY,SLASH})
    public Response error() {
        return Response.failure();
    }

}