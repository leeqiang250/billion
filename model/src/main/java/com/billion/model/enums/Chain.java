package com.billion.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author liqiang
 */
@Getter
@AllArgsConstructor
public enum Chain {

    /**
     * APTOS
     */
    APTOS("aptos", "Aptos(我是描述,不要把我当作Key)"),
    ;

    final String code;

    final String desc;

    @Getter
    static Map<String, Chain> KV;

    static {
        KV = Stream.of(values()).collect(Collectors.toMap(e -> e.code, e -> e));
    }

    public static Chain of(String code) {
        return KV.getOrDefault(code, Chain.APTOS);
    }

    @Override
    public String toString() {
        return this.code;
    }

}