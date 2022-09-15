package com.billion.gateway.aptos.kiko;

import com.billion.model.response.Response;
import com.billion.service.aptos.AptosService;
import com.billion.service.aptos.kiko.IImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author liqiang
 */
@Slf4j
@RestController
@RequestMapping({"/", "/ping"})
public class PingController {

    @Value("${spring.application.name}")
    String applicationName;

    @Value("${spring.profiles.active}")
    String profilesActive;

    @Resource
    AptosService aptosService;

    @Resource
    IImageService imageService;

    @GetMapping(path = {"", "/", "/failure", "/success"})
    public Response ping() {
        log.info("{}", imageService.add("https://imagedelivery.net/3mRLd_IbBrrQFSP57PNsVw/76360568-5c54-4342-427d-68992ded7b00/public"));
        return Response.success(applicationName + ":" + profilesActive);
    }

}