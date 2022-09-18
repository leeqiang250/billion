package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.controller.IController;
import com.billion.model.service.ICacheService;
import com.billion.model.entity.Language;
import com.billion.service.aptos.kiko.LanguageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author liqiang
 */
@RestController
@RequestMapping("template")
@SuppressWarnings({"rawtypes"})
public class TemplateController implements IController<Language> {

    @Resource
    LanguageService languageService;

    @Override
    public ICacheService<Language> service() {
        return this.languageService;
    }

}