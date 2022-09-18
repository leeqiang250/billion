package com.billion.service.aptos.kiko;

import com.billion.model.service.ICacheService;
import com.billion.model.entity.Image;
import com.billion.service.aptos.ContextService;
import org.springframework.transaction.annotation.Transactional;

import static com.billion.model.constant.RequestPath.SLASH;
import static com.billion.model.constant.v1.RequestPathImageV1.IMAGE;

/**
 * @author liqiang
 */
public interface ImageService extends ICacheService<Image> {

    /**
     * add
     *
     * @param uri  uri
     * @param desc desc
     * @return Image
     */
    @Transactional(rollbackFor = Exception.class)
    default Image add(String uri, String desc) {
        Image image = Image.builder()
                .uri(uri)
                .proxy(SLASH)
                .desc(desc)
                .build();
        this.save(image);
        image.setProxy(ContextService.getKikoHost() + IMAGE + SLASH + image.getId());
        this.updateById(image);

        return image;
    }

}