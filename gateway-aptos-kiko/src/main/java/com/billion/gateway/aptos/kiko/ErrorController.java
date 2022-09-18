package com.billion.gateway.aptos.kiko;

import com.billion.model.response.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.billion.model.constant.RequestPathConstant.*;

/**
 * @author liqiang
 */
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping({EMPTY, SLASH, V1_ERROR})
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping({EMPTY, SLASH, "/error"})
    public Response error() {
        return Response.failure();
    }

}