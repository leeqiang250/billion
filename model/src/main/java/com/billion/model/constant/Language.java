package com.billion.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author liqiang
 */

@Getter
@AllArgsConstructor
public enum Language {

    CHT("cht", "繁體中文"),
    EN("en", "English"),
    ;

    private String code;
    private String desc;

    public static Language of(@NonNull String code) {
        for (Language language : values()) {
            if (code.equalsIgnoreCase(language.code)) {
                return language;
            }
        }

        return Language.CHT;
    }

    public static Map map() {
        return Stream.of(values()).collect(Collectors.toMap(item -> item.code, item -> item.desc));
    }

}