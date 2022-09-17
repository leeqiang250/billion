package com.billion.model.service;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liqiang
 */
public enum CacheTsType {

    CACHE_TS_SHORT("cache_ts_short", "短期缓存时间"),
    CACHE_TS_MIDDLE("cache_ts_middle", "中期缓存时间"),
    CACHE_TS_LONG("cache_ts_long", "长期缓存时间");

    private static final Map<String, CacheTsType> codeLookup = new ConcurrentHashMap(7);

    final String code;

    final String desc;

    private CacheTsType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public static CacheTsType fromCode(String code) {
        return codeLookup.getOrDefault(code, CACHE_TS_SHORT);
    }

    static {
        Iterator var0 = EnumSet.allOf(CacheTsType.class).iterator();

        while (var0.hasNext()) {
            CacheTsType type = (CacheTsType) var0.next();
            codeLookup.put(type.code, type);
        }

    }
}