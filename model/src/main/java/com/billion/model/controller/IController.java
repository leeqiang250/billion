package com.billion.model.controller;

import com.billion.model.dto.Context;
import com.billion.model.model.IModel;
import com.billion.model.response.Response;
import com.billion.model.service.ICacheService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.Serializable;
import java.util.Objects;

import static com.billion.model.constant.RequestPath.*;

/**
 * @author liqiang
 */
@SuppressWarnings({"rawtypes"})
public interface IController<T extends IModel> {

    /**
     * service
     *
     * @return ICacheService
     */
    default ICacheService<T> service() {
        return null;
    }

    /**
     * cacheGet
     *
     * @param context context
     * @return Response
     */
    @RequestMapping({LIST})
    default Response cacheList(@RequestHeader Context context) {
        return Objects.isNull(this.service())
                ? Response.INVALID
                : Response.success(this.service().cacheList(context));
    }

    /**
     * cacheGetMap
     *
     * @param context context
     * @return Response
     */
    @RequestMapping({EMPTY, SLASH, MAP})
    default Response cacheMap(@RequestHeader Context context) {
        return Objects.isNull(this.service())
                ? Response.INVALID
                : Response.success(this.service().cacheMap(context));
    }

    /**
     * cacheGetById
     *
     * @param context context
     * @param id      id
     * @return Response
     */
    @RequestMapping("/{id}")
    default Response cacheById(@RequestHeader Context context, @PathVariable Serializable id) {
        return Objects.isNull(this.service())
                ? Response.INVALID
                : Response.success(this.service().cacheById(context, id));
    }

}