package com.billion.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author jason
 */
@Getter
@AllArgsConstructor
@SuppressWarnings({"all"})
public enum TokenScene {

    MARKET("market", "market");

    final String code;

    final String desc;

    @Getter
    static final Map<String, TokenScene> KV0 = new HashMap<>(values().length);

    @Getter
    static final Map KV1 = new HashMap<>(values().length);

    static {
        Stream.of(values()).forEach(e -> KV0.put(e.code, e));
        Stream.of(values()).forEach(e -> KV1.put(e.code, e.desc));
    }

    public static TokenScene of(String code) {
        return KV0.getOrDefault(code, TokenScene.MARKET);
    }

    @Override
    public String toString() {
        return this.code;
    }

}