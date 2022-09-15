package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.extension.service.IService;
import com.billion.model.entity.Image;
import lombok.NonNull;

/**
 * @author liqiang
 */
public interface IImageService extends IService<Image>, IRedisService<Image> {

    /**
     * add
     *
     * @param uri uri
     * @return Image
     */
    Image add(@NonNull String uri);

}