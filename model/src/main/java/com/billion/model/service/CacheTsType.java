package com.billion.model.service;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author liqiang
 */
public enum CacheTsType {

    CACHE_TS_SHORT("cache_ts_short", "短期缓存时间"),
    CACHE_TS_MIDDLE("cache_ts_middle", "中期缓存时间"),
    CACHE_TS_LONG("cache_ts_long", "长期缓存时间");


    @Getter
    final String code;

    @Getter
    final String desc;

    CacheTsType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CacheTsType of(String code) {
        return kv.getOrDefault(code, CACHE_TS_SHORT);
    }

    static final Map<String, CacheTsType> kv = new HashMap<>(CacheTsType.values().length);

    static {
        Stream.of(CacheTsType.values()).forEach(e -> kv.put(e.getCode(), e));
    }

}