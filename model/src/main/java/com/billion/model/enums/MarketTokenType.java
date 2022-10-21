package com.billion.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum MarketTokenType {

    NFT("NFT", ""),
    BOX("BOX", ""),
    ;

    String type;
    String desc;

    @Getter
    static final Map<String, MarketTokenType> KV0 = new HashMap<>(values().length);

    @Getter
    static final Map KV1 = new HashMap<>(values().length);

    static {
        Stream.of(values()).forEach(e -> KV0.put(e.type, e));
        Stream.of(values()).forEach(e -> KV1.put(e.type, e.desc));
    }

    public static MarketTokenType of(String code) {
        return KV0.getOrDefault(code, MarketTokenType.NFT);
    }

    @Override
    public String toString() {
        return this.type;
    }
}
