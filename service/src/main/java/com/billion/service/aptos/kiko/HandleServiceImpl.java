package com.billion.service.aptos.kiko;

import com.billion.dao.aptos.kiko.HandleMapper;
import com.billion.model.entity.Handle;
import com.billion.service.aptos.AbstractCacheService;
import org.springframework.stereotype.Service;

/**
 * @author liqiang
 */
@Service
public class HandleServiceImpl extends AbstractCacheService<HandleMapper, Handle> implements HandleService {

}