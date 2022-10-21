package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.ContractMapper;
import com.billion.model.dto.Context;
import com.billion.model.entity.Contract;
import com.billion.model.enums.CacheTsType;
import com.billion.service.aptos.AbstractCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author liqiang
 */
@Slf4j
@Service
@SuppressWarnings({"rawtypes", "unchecked"})
public class ContractServiceImpl extends AbstractCacheService<ContractMapper, Contract> implements ContractService {

    @Override
    public Contract getByName(String name) {
        QueryWrapper<Contract> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Contract::getName, name);
        return super.getOne(wrapper);
    }

    @Override
    public Map cacheMap(Context context) {
        String key = this.cacheMapKey(context.getChain());

        Map map = this.getRedisTemplate().opsForHash().entries(key);
        if (!map.isEmpty()) {
            return map;
        }

        QueryWrapper<Contract> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Contract::getChain, context.getChain());
        List<Contract> list = super.list(wrapper);

        map = list.stream().collect(Collectors.toMap(Contract::getName, (e) -> {
            if (Objects.isNull(e.getModuleName())) {
                return e.getModuleAddress();
            } else {
                return e.getModuleAddress() + "::" + e.getModuleName();
            }
        }));

        this.getRedisTemplate().opsForHash().putAll(key, map);
        this.getRedisTemplate().expire(key, this.cacheSecond(CacheTsType.MIDDLE));

        return map;
    }

}