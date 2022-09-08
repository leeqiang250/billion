package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.extension.service.IService;
import com.billion.model.entity.Image;

/**
 * @author liqiang
 */
public interface IImageService extends IService<Image>, IRedisService<Image> {

}