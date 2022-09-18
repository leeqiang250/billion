package com.billion.service.aptos.kiko;

import com.billion.dao.aptos.kiko.ImageMapper;
import com.billion.model.dto.Context;
import com.billion.model.entity.Image;
import com.billion.service.aptos.AbstractCacheService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;

/**
 * @author liqiang
 */
@Service
public class ImageServiceImpl extends AbstractCacheService<ImageMapper, Image> implements ImageService {

    @Override
    public Map<Serializable, Image> cacheMap(Context context) {
        return super.cacheMap(context);
    }
}