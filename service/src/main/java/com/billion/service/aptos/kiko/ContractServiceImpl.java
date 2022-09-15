package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.billion.dao.aptos.kiko.ContractMapper;
import com.billion.model.constant.RedisPathConstant;
import com.billion.model.dto.Header;
import com.billion.model.entity.Contract;
import com.billion.service.aptos.ContextService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Resource
    ContextService contextService;

    public Map getContract(@NonNull Header header) {
        Map map = this.getRedisTemplate().opsForHash().entries(RedisPathConstant.CONTRACT + header.getChain().getCode());
        if (!map.isEmpty()) {
            return map;
        }
        QueryWrapper<Contract> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Contract::getChain, header.getChain().getCode());
        List<Contract> list = this.getBaseMapper().selectList(wrapper);
        map = list.stream().collect(Collectors.toMap(Contract::getName, Contract::getContract, (key1, key2) -> key2));
        if (this.contextService.isProd()) {
            this.getRedisTemplate().opsForHash().putAll(RedisPathConstant.CONTRACT + header.getChain().getCode(), map);
            this.getRedisTemplate().expire(RedisPathConstant.CONTRACT + header.getChain().getCode(), Duration.ofHours(1L));
        }
        return map;
    }

}