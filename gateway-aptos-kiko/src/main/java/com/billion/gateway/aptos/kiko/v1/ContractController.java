package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.controller.IController;
import com.billion.model.service.ICacheService;
import com.billion.model.dto.Context;
import com.billion.model.entity.Contract;
import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.ContractService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.billion.model.constant.RequestPathConstant.*;

/**
 * @author liqiang
 */
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping(V1_CONTRACT)
public class ContractController implements IController<Contract> {

    @Resource
    ContractService contractService;

    @Override
    public ICacheService<Contract> service() {
        return this.contractService;
    }

    @Override
    public Response cacheGet(Context context) {
        return Response.success(this.contractService.cacheMap(context));
    }

}