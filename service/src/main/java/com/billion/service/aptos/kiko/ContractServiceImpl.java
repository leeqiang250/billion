package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.ContractMapper;
import com.billion.model.constant.RedisPathConstant;
import com.billion.model.dto.Context;
import com.billion.model.entity.Contract;
import com.billion.service.aptos.ContextService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liqiang
 */
@Slf4j
@Service

public class ContractServiceImpl extends RedisServiceImpl<ContractMapper, Contract> implements ContractService {

    public Map getAll(@NonNull Context context) {
        String key = RedisPathConstant.CONTRACT + context.getChain();
        Map map = this.getRedisTemplate().opsForHash().entries(key);
        if (!map.isEmpty()) {
            return map;
        }

        QueryWrapper<Contract> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Contract::getChain, context.getChain());
        List<Contract> list = this.getBaseMapper().selectList(wrapper);
        map = list.stream().collect(Collectors.toMap(Contract::getName, Contract::getContract, (key1, key2) -> key2));
        this.getRedisTemplate().opsForHash().putAll(key, map);
        this.getRedisTemplate().expire(key, Duration.ofHours(ContextService.getCacheMiddle()));

        return map;
    }

}