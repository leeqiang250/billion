package com.billion.gateway.aptos.kiko;

import com.billion.model.response.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.billion.model.constant.RequestPath.SLASH;
import static com.billion.model.constant.RequestPathError.ERROR;

/**
 * @author liqiang
 */
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping({ SLASH, ERROR})
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping({SLASH, "/error"})
    public Response error() {
        return Response.FAILURE;
    }

}