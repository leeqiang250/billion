package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.billion.dao.aptos.kiko.OperationMapper;
import com.billion.model.dto.Context;
import com.billion.model.entity.Operation;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jason
 */
@Service
public class OperationServiceImpl extends ServiceImpl<OperationMapper, Operation> implements OperationService {


    @Override
    public List<Operation> getListById(Context context, String tokenId) {
        QueryWrapper<Operation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Operation::getChain, context.getChain());
        queryWrapper.lambda().eq(Operation::getTokenId, tokenId);

        return this.list(queryWrapper);
    }


}
