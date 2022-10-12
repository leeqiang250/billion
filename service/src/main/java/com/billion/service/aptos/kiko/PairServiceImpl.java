package com.billion.service.aptos.kiko;

import com.billion.dao.aptos.kiko.PairMapper;
import com.billion.model.entity.Pair;
import com.billion.service.aptos.AbstractCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class PairServiceImpl extends AbstractCacheService<PairMapper, Pair> implements PairService {

}