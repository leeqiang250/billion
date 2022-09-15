package com.billion.model.constant;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * @author liqiang
 */
@AllArgsConstructor
public enum Chain {

    APTOS("aptos"),
    ;

    String code;

    public String code() {
        return this.code;
    }

    public static Chain of(@NonNull String code) {
        for (Chain e : values()) {
            if (e.code.equalsIgnoreCase(code)) {
                return e;
            }
        }

        return Chain.APTOS;
    }

}