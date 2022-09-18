package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.constant.RedisPathConstant;
import com.billion.model.dto.Context;
import com.billion.model.entity.Image;
import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.ImageService;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

import static com.billion.model.constant.RequestPathConstant.V1_IMAGE;

/**
 * @author liqiang
 */
@RestController
@RequestMapping({V1_IMAGE})
@SuppressWarnings({"rawtypes"})
public class ImageController {

    @Resource
    ImageService imageService;

    @RequestMapping("/{id}")
    public Response get(@RequestHeader Context context, @PathVariable Long id, HttpServletResponse response) throws IOException {
        Image image = this.imageService.cacheById(context, RedisPathConstant.IMAGE, id);
        if (!Objects.isNull(image) && StringUtils.isNotEmpty(image.getUri())) {
            response.sendRedirect(image.getUri());
        }

        return Response.failure();
    }

}