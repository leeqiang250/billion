package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.controller.IController;
import com.billion.model.service.IService;
import com.billion.model.dto.Context;
import com.billion.model.entity.Language;
import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.LanguageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.io.Serializable;

import static com.billion.model.constant.RequestPathConstant.*;

/**
 * @author liqiang
 */
@RestController
@RequestMapping(V1_LANGUAGE)
public class LanguageController implements IController<Language> {

    @Resource
    LanguageService languageService;

    @Override
    public IService<Language> service() {
        return this.languageService;
    }

    @Override
    public Response get(Context context) {
        return Response.success(this.languageService.getAll(context));
    }

    @Override
    public Response get(Serializable id, Context context) {
        return Response.success(this.languageService.getByKey(id.toString(), context));
    }

}