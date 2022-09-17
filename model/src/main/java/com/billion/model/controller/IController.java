package com.billion.model.controller;

import com.billion.model.dto.Context;
import com.billion.model.response.Response;
import com.billion.model.service.IService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.Serializable;

import static com.billion.model.constant.RequestPathConstant.EMPTY;
import static com.billion.model.constant.RequestPathConstant.SLASH;

/**
 * @author liqiang
 */
public interface IController<T> {

    /**
     * service
     *
     * @return
     */
    IService<T> service();

    /**
     * get
     *
     * @return Response
     */
    @RequestMapping({EMPTY, SLASH})
    default Response get(@RequestHeader Context context) {
        return Response.success(this.service().list());
    }

    /**
     * get
     *
     * @param id id
     * @return Response
     */
    @RequestMapping("/{id}")
    default Response get(@PathVariable Serializable id, @RequestHeader Context context) {
        return Response.success(this.service().getById(id));
    }

}