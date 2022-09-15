package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.dto.Header;
import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.ContractService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.billion.model.constant.RequestPathConstant.V1_CONTRACT;

/**
 * @author liqiang
 */
@RestController
@RequestMapping(V1_CONTRACT)
public class ContractController {

    @Resource
    ContractService contractService;

    @GetMapping({"", "/"})
    public Response get(@RequestHeader Header header) {
        return Response.success(this.contractService.getContract(header));
    }

}