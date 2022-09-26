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
public enum TransactionStatus {

    STATUS_0_PREPARING("status_0_preparing", "交易准备中"),
    STATUS_1_READY("status_1_ready", "交易准备完成"),
    STATUS_2_ING("status_2_ing", "交易中"),
    STATUS_3_SUCCESS("status_3_success", "交易成功"),
    STATUS_4_FAILURE("status_4_failure", "交易失败");

    final String code;

    final String desc;

    @Getter
    static final Map<String, TransactionStatus> KV0 = new HashMap<>(values().length);

    @Getter
    static final Map KV1 = new HashMap<>(values().length);

    static {
        Stream.of(values()).forEach(e -> KV0.put(e.code, e));
        Stream.of(values()).forEach(e -> KV1.put(e.code, e.desc));
    }

    public static TransactionStatus of(String code) {
        return KV0.getOrDefault(code, TransactionStatus.STATUS_0_PREPARING);
    }

    @Override
    public String toString() {
        return this.code;
    }

}