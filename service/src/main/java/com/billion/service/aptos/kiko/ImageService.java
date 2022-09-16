package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.extension.service.IService;
import com.billion.model.constant.RequestPathConstant;
import com.billion.model.entity.Image;
import com.billion.service.aptos.ContextService;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import static com.billion.model.constant.RequestPathConstant.SLASH;
import static com.billion.model.constant.RequestPathConstant.V1_IMAGE;

/**
 * @author liqiang
 */
public interface ImageService extends IService<Image>, RedisService<Image> {

    /**
     * add
     *
     * @param uri  uri
     * @param desc desc
     * @return Image
     */
    @Transactional(rollbackFor = Exception.class)
    default Image add(@NonNull String uri, @NonNull String desc) {
        Image image = Image.builder()
                .uri(uri)
                .proxy(RequestPathConstant.EMPTY)
                .desc(desc)
                .build();
        this.save(image);
        image.setProxy(ContextService.getKikoHost() + V1_IMAGE + SLASH + image.getId());
        this.updateById(image);

        return image;
    }

}