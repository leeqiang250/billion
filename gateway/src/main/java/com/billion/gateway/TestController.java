package com.billion.gateway;

import com.billion.model.response.Response;
import com.billion.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author liqiang
 */
@RestController
@RequestMapping(path = {"/test"})
public class TestController {

    @Resource
    TestService testService;

    @GetMapping("/failure")
    public Response failure() {
        return Response.success(testService.list());
    }

}