package com.billion.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author liqiang
 */

@AllArgsConstructor
public enum AuthenticateType {

    PUBLIC("PUBLIC", "公开的"),
    PROTECT("PROTECT", "受保护的，普通鉴权"),
    PRIVATE("private", "私有的，私钥签名"),
    FORBID("forbid", "禁止的"),
    ;

    final String code;

    final String desc;

    @Getter
    static final Map<String, AuthenticateType> KV0 = new HashMap<>(values().length);

    @Getter
    static final Map KV1 = new HashMap<>(values().length);

    static {
        Stream.of(values()).forEach(e -> KV0.put(e.code, e));
        Stream.of(values()).forEach(e -> KV1.put(e.code, e.desc));
    }

    public static AuthenticateType of(String code) {
        return KV0.getOrDefault(code, AuthenticateType.PROTECT);
    }

    @Override
    public String toString() {
        return this.code;
    }

}