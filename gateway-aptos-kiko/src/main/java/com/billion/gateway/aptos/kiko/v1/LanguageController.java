package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.LanguageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{language}")
    public Response get(@PathVariable("language") String language) {
        return Response.success(languageService.getLanguage(language));
    }

}