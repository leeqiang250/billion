package com.billion.gateway.aptos.cms.v1;

import com.billion.model.dto.Context;
import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.ContractService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.billion.model.constant.RequestPathConstant.*;

/**
 * @author liqiang
 */
@RestController
@RequestMapping(V1_CONTRACT)
public class ContractController {

    @Resource
    ContractService contractService;

    @RequestMapping({EMPTY,SLASH})
    public Response get(@RequestHeader Context context) {
        return Response.success(this.contractService.getAll(context));
    }

}