package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.billion.dao.aptos.kiko.ImageMapper;
import com.billion.model.entity.Image;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author liqiang
 */
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {

    @Override
    public Image getById(Serializable id) {
        Image image = super.getById(id);
        if (Objects.isNull(image)) {
            image = Image.builder().id(Long.valueOf(id.toString())).build();
        }
        if (Objects.isNull(image.getUri()) || "".equals(image.getUri())) {
            image.setUri(null);
        }
        //TODO 缓存
        System.out.println(image);
        return image;
    }

}