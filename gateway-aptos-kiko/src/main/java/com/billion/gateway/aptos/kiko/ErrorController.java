package com.billion.gateway.aptos.kiko;

import com.billion.model.response.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.billion.model.constant.RequestPath.EMPTY;
import static com.billion.model.constant.RequestPath.SLASH;
import static com.billion.model.constant.RequestPathError.*;

/**
 * @author liqiang
 */
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping({ SLASH, ERROR})
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping({EMPTY,SLASH, ERROR})
    public Response failure() {
        return Response.FAILURE;
    }

    @RequestMapping({INVALID})
    public Response invalid() {
        return Response.INVALID;
    }

    @RequestMapping({INVALID_TOKEN})
    public Response invalidToken() {
        return Response.INVALID_TOKEN;
    }

    @RequestMapping({FORBID})
    public Response forbid() {
        return Response.FORBID;
    }

}