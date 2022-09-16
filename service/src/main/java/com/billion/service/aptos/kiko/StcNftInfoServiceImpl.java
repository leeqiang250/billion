package com.billion.service.aptos.kiko;

import com.billion.dao.aptos.kiko.StcNftInfoMapper;
import com.billion.model.entity.StcNftInfo;
import org.springframework.stereotype.Service;

/**
 * @author liqiang
 */
@Service
public class StcNftInfoServiceImpl extends RedisServiceImpl<StcNftInfoMapper, StcNftInfo> implements StcNftInfoService {

}