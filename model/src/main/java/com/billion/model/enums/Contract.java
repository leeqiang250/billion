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
public enum Contract {

    BOX_PRIMARY_MARKET("box_primary_market", "box_primary_market"),

    XXX("xxx", "xxx");

    final String code;

    final String desc;

    static final Map<String, Contract> KV0 = new HashMap<>(values().length);

    @Getter
    static final Map KV1 = new HashMap<>(values().length);

    static {
        Stream.of(values()).forEach(e -> KV0.put(e.code, e));
        Stream.of(values()).forEach(e -> KV1.put(e.code, e.desc));
    }

    public static Contract of(String code) {
        return KV0.getOrDefault(code, Contract.BOX_PRIMARY_MARKET);
    }

    @Override
    public String toString() {
        return this.code;
    }

}