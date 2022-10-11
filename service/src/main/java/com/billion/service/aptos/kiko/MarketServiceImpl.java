package com.billion.service.aptos.kiko;

import com.billion.dao.aptos.kiko.MarketMapper;
import com.billion.model.entity.Market;
import com.billion.service.aptos.AbstractCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author liqiang
 */

@Slf4j
@Service
public class MarketServiceImpl extends AbstractCacheService<MarketMapper, Market> implements MarketService {

}