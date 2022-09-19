package com.billion.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author liqiang
 */
@Getter
@AllArgsConstructor
public enum CacheTsType {

    /**
     *
     */
    SHORT("short", "短期缓存时间"),

    /**
     *
     */
    MIDDLE("middle", "中期缓存时间"),

    /**
     *
     */
    LONG("long", "长期缓存时间");

    final String code;

    final String desc;

    @Getter
    static final Map<String, CacheTsType> KV = new HashMap<>(CacheTsType.values().length);

    static {
        Stream.of(CacheTsType.values()).forEach(e -> KV.put(e.getCode(), e));
    }

    public static CacheTsType of(String code) {
        return KV.getOrDefault(code, SHORT);
    }

    @Override
    public String toString() {
        return this.code;
    }
}