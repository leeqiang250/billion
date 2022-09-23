package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Resource;
import com.billion.model.entity.Token;
import com.billion.model.service.ICacheService;

import java.io.Serializable;

/**
 * @author liqiang
 */
public interface TokenService extends ICacheService<Token> {


    /**
     * initialize
     *
     * @param id id
     * @return boolean
     */
    boolean initialize(Serializable id);


    /**
     * transferResource
     *
     * @param from     from
     * @param to       to
     * @param amount   amount
     * @param resource resource
     * @return boolean
     */
    boolean transferResource(String from, String to, String amount, Resource resource);

}