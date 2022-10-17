package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.extension.service.IService;
import com.billion.model.dto.Context;
import com.billion.model.entity.Operation;

import java.util.List;

/**
 * @author jason
 */
public interface OperationService extends IService<Operation> {

    /**
     * getListById
     * @param context
     * @param tokenId
     * @return
     */
    public List<Operation> getListById(Context context, String tokenId);
}
