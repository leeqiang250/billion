package com.billion.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqiang
 */
@Getter
@AllArgsConstructor
public enum BizErrorCode {

    /**
     * 系统个错误
     */
    SYSTEM_ERROR(100000, "system error"),

    /**
     * 错误的URL
     */
    INVALID_URL(100001, "invalid url"),

    /**
     * 签名无效
     */
    BAD_SIGNATURE(100002, "bad signature"),

    /**
     * API签名无效
     */
    API_SIGNATURE_NOT_VALID(100003, "api signature not valid"),

    /**
     * 数据验证失败
     */
    DATA_BIND_VALIDATION_FAILURE(100004, "data bind validation failure"),

    /**
     * 数据绑定失败
     */
    DATA_VALIDATION_FAILURE(100005, "data validation failure"),

    /**
     * 签名机请求失败
     */
    SIGN_CALL_FAILURE(100016, "sign call failure");

    final int code;

    final String message;

    @Override
    public String toString() {
        return String.valueOf(this.code);
    }

}