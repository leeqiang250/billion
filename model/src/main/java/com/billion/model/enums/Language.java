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
@SuppressWarnings({"rawtypes", "unchecked"})
public enum Language {

    /**
     * ZH_TC
     */
    ZH_TC("zh-TC", "繁體中文(我是描述,不要把我当作Key)"),

    /**
     * EN
     */
    EN("en", "English(我是描述,不要把我当作Key)"),
    ;

    final String code;

    final String desc;

    static final Map<String, Language> KV0 = new HashMap<>(values().length);

    @Getter
    static final Map KV1 = new HashMap<>(values().length);

    static {
        Stream.of(values()).forEach(e -> KV0.put(e.code, e));
        Stream.of(values()).forEach(e -> KV1.put(e.code, e.desc));
    }

    public static Language of(String code) {
        return KV0.getOrDefault(code, Language.ZH_TC);
    }

    @Override
    public String toString() {
        return this.code;
    }

}