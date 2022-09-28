package com.billion.service.aptos.kiko;

import com.billion.dao.aptos.kiko.NftEventMapper;
import com.billion.model.entity.NftEvent;
import com.billion.service.aptos.AbstractCacheService;
import org.springframework.stereotype.Service;

/**
 * @author liqiang
 */
@Service
public class NftEventServiceImpl extends AbstractCacheService<NftEventMapper, NftEvent> implements NftEventService {

}
