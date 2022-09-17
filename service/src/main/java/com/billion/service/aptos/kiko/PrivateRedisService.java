package com.billion.service.aptos.kiko;

import java.util.Map;

/**
 * 只有CMS可以使用
 *
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
