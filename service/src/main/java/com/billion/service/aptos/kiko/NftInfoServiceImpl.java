package com.billion.service.aptos.kiko;

import com.billion.dao.aptos.kiko.NftInfoMapper;
import com.billion.model.entity.NftInfo;
import com.billion.service.aptos.AbstractCacheService;
import org.springframework.stereotype.Service;

/**
 * @author liqiang
 */
@Service
public class NftInfoServiceImpl extends AbstractCacheService<NftInfoMapper, NftInfo> implements NftInfoService {

}
