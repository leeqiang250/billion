package com.billion.model.service;

import com.billion.model.dto.Context;

import java.io.Serializable;
import java.util.List;

/**
 * @author liqiang
 */
public interface IService<T> extends com.baomidou.mybatisplus.extension.service.IService<T> {

    /**
     * list
     *
     * @param context context
     * @return List<T>
     */
    default List<T> list(Context context) {
        return this.list();
    }

    /**
     * getById
     *
     * @param id      id
     * @param context context
     * @return T
     */
    default T getById(Context context, Serializable id) {
        return this.getById(id);
    }

}