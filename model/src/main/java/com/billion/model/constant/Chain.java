package com.billion.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author liqiang
 */

@Getter
@AllArgsConstructor
public enum Chain {

    APTOS("aptos"),
    ;

    String code;

    public static Chain of(@NonNull String code) {
        for (Chain e : values()) {
            if (code.equalsIgnoreCase(e.code)) {
                return e;
            }
        }

        return Chain.APTOS;
    }

}