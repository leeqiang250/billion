package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.dto.Header;
import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.LanguageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.billion.model.constant.RequestPathConstant.V1_LANGUAGE;

/**
 * @author liqiang
 */
@RestController
@RequestMapping(V1_LANGUAGE)
public class LanguageController {

    @Resource
    LanguageService languageService;

    @RequestMapping({"", "/"})
    public Response get(@RequestHeader Header header) {
        return Response.success(this.languageService.getLanguage(header));
    }

}