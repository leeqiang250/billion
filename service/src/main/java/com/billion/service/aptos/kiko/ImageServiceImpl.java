package com.billion.service.aptos.kiko;

import com.billion.dao.aptos.kiko.ImageMapper;
import com.billion.model.entity.Image;
import com.billion.service.aptos.AbstractCacheService;
import org.springframework.stereotype.Service;

/**
 * @author liqiang
 */
@Service
public class ImageServiceImpl extends AbstractCacheService<ImageMapper, Image> implements ImageService {

}