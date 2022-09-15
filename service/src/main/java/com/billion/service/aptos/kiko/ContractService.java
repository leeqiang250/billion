package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.extension.service.IService;
import com.billion.model.entity.Contract;
import lombok.NonNull;

import java.util.Map;

/**
 * @author liqiang
 */
public interface ContractService extends IService<Contract> {

    /**
     * getContract
     *
     * @param chain chain
     * @return Map
     */
    Map getContract(@NonNull String chain);

}
