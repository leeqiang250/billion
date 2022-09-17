package com.billion.service.aptos.kiko;

import java.util.Map;

/**
 * @author liqiang
 */
public interface PrivateRedisService {

    /**
     * get
     *
     * @return Map
     */
    Map get();

    /**
     * delete
     *
     * @param key key
     * @return Boolean
     */
    Boolean delete(String key);

}
