package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.controller.IController;
import com.billion.model.controller.IService;
import com.billion.model.dto.Context;
import com.billion.model.entity.Contract;
import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.ContractService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.io.Serializable;

import static com.billion.model.constant.RequestPathConstant.*;

/**
 * @author liqiang
 */
@RestController
@RequestMapping(V1_CONTRACT)
public class ContractController implements IController<Contract> {

    @Resource
    ContractService contractService;

    @Override
    public IService<Contract> service() {
        return this.contractService;
    }

    @Override
    public Response get(Context context) {
        return Response.success(this.contractService.getAll(context));
    }

    @Override
    public Response get(Serializable id, Context context) {
        return Response.failure();
    }

}