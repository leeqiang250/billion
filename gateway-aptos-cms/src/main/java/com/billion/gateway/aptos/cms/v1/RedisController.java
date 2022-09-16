package com.billion.gateway.aptos.cms.v1;

import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.PrivateRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

import static com.billion.model.constant.RequestPathConstant.*;

/**
 * @author liqiang
 */
@Slf4j
@RestController
@RequestMapping(V1_REDIS)
public class RedisController {

    @Resource
    PrivateRedisService privateRedisService;

    @RequestMapping({EMPTY, SLASH})
    public Map get() {
        return privateRedisService.get();
    }

    @RequestMapping("/{key}")
    public Response temp(@PathVariable String key) {
        return Response.success(privateRedisService.delete(key));
    }


}
