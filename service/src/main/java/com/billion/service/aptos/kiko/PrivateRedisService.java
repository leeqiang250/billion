package com.billion.service.aptos.kiko;

import lombok.NonNull;

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
    Boolean delete(@NonNull String key);

}
