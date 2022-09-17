package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.controller.IController;
import com.billion.model.controller.IService;
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
public class TemplateController implements IController<Language> {

    @Resource
    LanguageService languageService;


    @Override
    public IService<Language> service() {
        return this.languageService;
    }

}