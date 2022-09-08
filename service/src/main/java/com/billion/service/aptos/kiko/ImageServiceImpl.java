package com.billion.service.aptos.kiko;

import com.billion.dao.aptos.kiko.ImageMapper;
import com.billion.model.entity.Image;
import org.springframework.stereotype.Service;

/**
 * @author liqiang
 */
@Service
public class ImageServiceImpl extends RedisServiceImpl<ImageMapper, Image> implements IImageService {

}