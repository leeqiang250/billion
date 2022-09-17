package com.billion.model.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.billion.model.response.Response;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
    default Response get() {
        return Response.success(this.service().list());
    }

    /**
     * get
     *
     * @param id id
     * @return Response
     */
    @RequestMapping("/{id}")
    default Response get(@PathVariable Long id) {
        return Response.success(this.service().getById(id));
    }

}