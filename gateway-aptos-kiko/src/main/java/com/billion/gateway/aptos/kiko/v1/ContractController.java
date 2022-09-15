package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.ContractService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{chain}")
    public Response get(@PathVariable("chain") String chain) {
        return Response.success(this.contractService.getContract(chain));
    }

}