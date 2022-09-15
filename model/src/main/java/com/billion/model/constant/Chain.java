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

    private String code;

    public static Chain of(@NonNull String code) {
        for (Chain language : values()) {
            if (code.equalsIgnoreCase(language.code)) {
                return language;
            }
        }

        return Chain.APTOS;
    }

}