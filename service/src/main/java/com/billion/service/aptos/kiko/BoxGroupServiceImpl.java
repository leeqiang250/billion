package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.billion.dao.aptos.kiko.BoxGroupMapper;
import com.billion.model.entity.BoxGroup;
import com.billion.service.aptos.AbstractCacheService;
import org.springframework.stereotype.Service;

/**
 * @author jason
 */
@Service
public class BoxGroupServiceImpl extends AbstractCacheService<BoxGroupMapper, BoxGroup> implements BoxGroupService {

}
