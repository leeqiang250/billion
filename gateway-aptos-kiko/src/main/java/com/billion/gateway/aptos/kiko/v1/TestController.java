package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.entity.Test;
import com.billion.model.response.Response;
import com.billion.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

import static com.billion.gateway.aptos.kiko.v1.RequestPathConstant.V1_TEST;

/**
 * @author liqiang
 */
@RestController
@RequestMapping({V1_TEST})
public class TestController {

    @Resource
    TestService testService;

    @GetMapping("/list")
    public Response<List<Test>> list() {
        testService.save(Test.builder().name(UUID.randomUUID().toString()).build());
        return Response.success(testService.list());
    }

}