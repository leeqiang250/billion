package com.billion.service.aptos.kiko;

import com.billion.dao.aptos.kiko.ImageMapper;
import com.billion.model.entity.Image;
import com.billion.service.aptos.ContextService;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.billion.model.constant.RequestPathConstant.V1_IMAGE;

/**
 * @author liqiang
 */
@Service
public class ImageServiceImpl extends RedisServiceImpl<ImageMapper, Image> implements ImageService {

    @Resource
    ContextService contextService;

    @Override
    @Transactional
    public Image add(@NonNull String uri) {
        Image image = Image.builder()
                .uri(uri)
                .proxy("")
                .build();
        this.save(image);
        image.setProxy(this.contextService.getKikoHost() + V1_IMAGE + "/" + image.getId());
        this.updateById(image);

        return image;
    }

}