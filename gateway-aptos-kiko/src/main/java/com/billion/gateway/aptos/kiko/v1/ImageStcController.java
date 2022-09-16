package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.constant.RedisPathConstant;
import com.billion.model.entity.Image;
import com.billion.model.entity.StcNftGroup;
import com.billion.model.entity.StcNftInfo;
import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.ImageService;
import com.billion.service.aptos.kiko.StcNftGroupService;
import com.billion.service.aptos.kiko.StcNftInfoService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

import static com.billion.model.constant.RequestPathConstant.V1_IMAGE;

/**
 * @author liqiang
 */
@RestController
@RequestMapping({V1_IMAGE + "sdfdsf"})
public class ImageStcController {

    @Resource
    ImageService imageService;

    @Resource
    StcNftGroupService stcNftGroupService;

    @Resource
    StcNftInfoService stcNftInfoService;

    @RequestMapping("/group/{id}")
    public Response getGroupImage(HttpServletResponse response, @PathVariable("id") long id) throws IOException {
        StcNftGroup model = this.stcNftGroupService.getCacheById(RedisPathConstant.IMAGE_STC, id);
        if (!Objects.isNull(model) && !Objects.isNull(model.getNftTypeImageLink()) && !"".equals(model.getNftTypeImageLink())) {
            response.sendRedirect(model.getNftTypeImageLink());
        }

        return Response.success();
    }

    @RequestMapping("/info/{id}")
    public Response getInfoImage(HttpServletResponse response, @PathVariable("id") long id) throws IOException {
        StcNftInfo model = this.stcNftInfoService.getCacheById(RedisPathConstant.IMAGE_STC, id);
        if (!Objects.isNull(model) && !Objects.isNull(model.getImageLink()) && !"".equals(model.getImageLink())) {
            response.sendRedirect(model.getImageLink());
        }

        return Response.success();
    }

}