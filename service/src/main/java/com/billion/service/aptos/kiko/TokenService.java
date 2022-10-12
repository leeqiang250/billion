package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Resource;
import com.billion.model.dto.Context;
import com.billion.model.entity.Token;
import com.billion.model.service.ICacheService;

import java.util.List;

/**
 * @author liqiang
 */
public interface TokenService extends ICacheService<Token> {

    /**
     * initialize
     *
     * @return boolean
     */
    boolean initialize();


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


    /**
     * getByScene
     *
     * @param context context
     * @param scene   scene
     * @return List<Token>
     */
    List<Token> getByScene(Context context, String scene);

}