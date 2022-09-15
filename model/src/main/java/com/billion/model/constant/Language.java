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
public enum Language {

    //ZHCN("zh-CN", "简体中文"),
    ZH_TC("zh-TC", "繁體中文(我是描述,不要把我当作Key)"),
    EN("en", "English(我是描述,不要把我当作Key)"),
    ;

    String code;

    String desc;

    public String code() {
        return this.code;
    }

    public String desc() {
        return this.desc;
    }

    public static Language of(@NonNull String code) {
        for (Language e : values()) {
            if (e.code.equalsIgnoreCase(code)) {
                return e;
            }
        }

        return Language.ZH_TC;
    }

    public static Map map() {
        return Stream.of(values()).collect(Collectors.toMap(e -> e.code, e -> e.desc));
    }

}