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
@SuppressWarnings({"all"})
public enum Chain {

    APTOS("aptos", "Aptos(我是描述,不要把我当作Key)"),
    ;

    final String code;

    final String desc;

    @Getter
    static final Map<String, Chain> KV0 = new HashMap<>(values().length);

    @Getter
    static final Map KV1 = new HashMap<>(values().length);

    static {
        Stream.of(values()).forEach(e -> KV0.put(e.code, e));
        Stream.of(values()).forEach(e -> KV1.put(e.code, e.desc));
    }

    public static Chain of(String code) {
        return KV0.getOrDefault(code, Chain.APTOS);
    }

    @Override
    public String toString() {
        return this.code;
    }

}