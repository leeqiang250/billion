package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.entity.Image;
import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.ImageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

import static com.billion.gateway.aptos.kiko.v1.RequestPathConstant.V1_IMAGE;

/**
 * @author liqiang
 */
@RestController
@RequestMapping({V1_IMAGE})
public class ImageController {

    @Resource
    ImageService imageService;

    @GetMapping("/{id}")
    public Response get(HttpServletResponse response, @PathVariable("id") long id) throws IOException {
        Image image = imageService.getById(id);
        if (!Objects.isNull(image) && !Objects.isNull(image.getUri())) {
            response.sendRedirect(image.getUri());
        }
        return Response.success();
    }

}