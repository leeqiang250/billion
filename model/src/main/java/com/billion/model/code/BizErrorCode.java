package com.billion.model.code;


/**
 * @author liqiang
 */
public enum BizErrorCode implements ErrorCode {

    /**
     * 系统个错误
     */
    SYSTEM_ERROR(100000, "system error"),

    /**
     * 错误的URL
     */
    INVALID_URL(100001, "invalid url"),

    /**
     * URL签名无效
     */
    BAD_SIGNATURE(100002, "bad signature"),

    /**
     * API签名无效: %s
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

    private final int code;

    private final String message;

    BizErrorCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return "[" + this.getCode() + "]" + this.getMessage();
    }

}