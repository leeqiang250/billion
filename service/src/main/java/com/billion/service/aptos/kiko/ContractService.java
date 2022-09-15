package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.extension.service.IService;
import com.billion.model.dto.Context;
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
     * @param context context
     * @return Map
     */
    Map getContract(@NonNull Context context);

}
