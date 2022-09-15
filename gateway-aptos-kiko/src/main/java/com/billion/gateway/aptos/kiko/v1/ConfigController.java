package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.dto.Header;
import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.ConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.billion.model.constant.RequestPathConstant.V1_CONFIG;

/**
 * @author liqiang
 */
@RestController
@RequestMapping(V1_CONFIG)
public class ConfigController {

    @Resource
    ConfigService configService;

    @GetMapping({"", "/"})
    public Response get(@RequestHeader Header header) {
        return Response.success(configService.get(header));
    }

}