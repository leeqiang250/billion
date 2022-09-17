package com.billion.gateway.aptos.cms.v1;

import com.billion.model.constant.RedisPathConstant;
import com.billion.model.entity.StcNftGroup;
import com.billion.model.entity.StcNftInfo;
import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.StcNftGroupService;
import com.billion.service.aptos.kiko.StcNftInfoService;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

import static com.billion.model.constant.RequestPathConstant.STC_IMAGE;

/**
 * @author liqiang
 */
@Slf4j
@RestController
@RequestMapping({STC_IMAGE})
public class ImageStcController {

    @Resource
    StcNftGroupService stcNftGroupService;

    @Resource
    StcNftInfoService stcNftInfoService;

    @RequestMapping("/group/{id}")
    public Response getGroupImage(HttpServletResponse response, @PathVariable Long id) throws IOException {
        StcNftGroup model = this.stcNftGroupService.getCacheById(RedisPathConstant.IMAGE_STC, id);
        if (!Objects.isNull(model) && StringUtils.isNotEmpty(model.getNftTypeImageLink())) {
            response.sendRedirect(model.getNftTypeImageLink());
        }

        return Response.failure();
    }

    @RequestMapping("/info/{id}")
    public Response getInfoImage(HttpServletResponse response, @PathVariable Long id) throws IOException {
        if (21657L <= id && id < 21731L) {
            log.info("3EYEDCat OLD:{} NEW:{}", id, id + 1L);
            id++;
        }

        StcNftInfo model = this.stcNftInfoService.getCacheById(RedisPathConstant.IMAGE_STC, id);
        if (!Objects.isNull(model) && StringUtils.isNotEmpty(model.getImageLink())) {
            response.sendRedirect(model.getImageLink());
        }

        return Response.failure();
    }

}