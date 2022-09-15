package com.billion.model.constant;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author liqiang
 */
@AllArgsConstructor
public enum Chain {

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

    public static Chain of(@NonNull String code) {
        for (Chain e : values()) {
            if (e.code.equalsIgnoreCase(code)) {
                return e;
            }
        }

        return Chain.APTOS;
    }

    public static Map map() {
        return Stream.of(values()).collect(Collectors.toMap(e -> e.code, e -> e.desc));
    }

}