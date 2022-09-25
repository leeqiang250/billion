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
public enum Mint {

    MINT_0_PREPARING("mint_0_preparing", "铸造准备中"),
    MINT_1_READY("mint_1_ready", "铸造准备完成"),
    MINT_2_ING("mint_2_ing", "铸造中"),
    MINT_3_SUCCESS("mint_3_success", "铸造成功"),
    MINT_4_FAILURE("mint_4_failure", "铸造失败");

    final String code;

    final String desc;

    @Getter
    static final Map<String, Mint> KV0 = new HashMap<>(values().length);

    @Getter
    static final Map KV1 = new HashMap<>(values().length);

    static {
        Stream.of(values()).forEach(e -> KV0.put(e.code, e));
        Stream.of(values()).forEach(e -> KV1.put(e.code, e.desc));
    }

    public static Mint of(String code) {
        return KV0.getOrDefault(code, Mint.MINT_0_PREPARING);
    }

    @Override
    public String toString() {
        return this.code;
    }

}