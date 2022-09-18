package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.response.Response;
import com.billion.service.aptos.ContextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.billion.model.constant.RequestPathConstant.STC_IMAGE;

/**
 * @author liqiang
 */
@Slf4j
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping({STC_IMAGE})
public class ImageStcController {

    @RequestMapping("/group/{id}")
    public Response getGroupImage(@PathVariable String id, HttpServletResponse response) throws IOException {
        response.sendRedirect(ContextService.getKikoStcImageGroupApi() + id);

        return Response.failure();
    }

    @RequestMapping("/info/{id}")
    public Response getInfoImage(@PathVariable String id, HttpServletResponse response) throws IOException {
        response.sendRedirect(ContextService.getKikoStcImageInfoApi() + id);

        return Response.failure();
    }

}