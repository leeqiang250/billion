package com.billion.model.constant;

/**
 * @author liqiang
 */
public interface RedisPath {

    String SCAN_CHAIN_LOCK = "scan_chain_lock";

    long LOCK_EXPIRE_TS = 30 * 1000L;

}