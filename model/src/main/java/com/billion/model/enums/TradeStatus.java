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
public enum TradeStatus {

    STATUS_0_BIDDING("status_0_bidding", "交易准备中"),

    STATUS_1_COMPLETE("status_1_complete", "交易准备完成"),

    STATUS_2_CANCEL("status_2_cancel", "交易中");

    final String code;

    final String desc;

    @Getter
    static final Map<String, TradeStatus> KV0 = new HashMap<>(values().length);

    @Getter
    static final Map KV1 = new HashMap<>(values().length);

    static {
        Stream.of(values()).forEach(e -> KV0.put(e.code, e));
        Stream.of(values()).forEach(e -> KV1.put(e.code, e.desc));
    }

    public static TradeStatus of(String code) {
        return KV0.getOrDefault(code, TradeStatus.STATUS_0_BIDDING);
    }

    @Override
    public String toString() {
        return this.code;
    }
}