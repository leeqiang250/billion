package com.billion.model.constant;

import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author liqiang
 */
@AllArgsConstructor
public enum Chain {

    /**
     * APTOS
     */
    APTOS("aptos", "Aptos(我是描述,不要把我当作Key)"),
    ;

    String code;

    String desc;

    public String code() {
        return this.code;
    }

    public String desc() {
        return this.desc;
    }

    public static Chain of(String code) {
        map();
        return mapV2.getOrDefault(code, Chain.APTOS);
    }

    static Map mapV1;

    static Map<String, Chain> mapV2;

    public static Map map() {
        if (Objects.isNull(mapV1)) {
            mapV1 = Stream.of(values()).collect(Collectors.toMap(e -> e.code, e -> e.desc));
            mapV2 = Stream.of(values()).collect(Collectors.toMap(e -> e.code, e -> e));
        }
        return mapV1;
    }

}