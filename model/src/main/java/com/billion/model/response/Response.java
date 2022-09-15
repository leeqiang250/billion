package com.billion.model.response;

import com.billion.model.code.ErrorCode;
import lombok.*;

import java.io.Serializable;

/**
 * @author liqiang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Response<T> implements Serializable {

    int code;

    long ts;

    String msg;

    T data;

    public static Response success() {
        return success(null);
    }

    public static <T> Response success(final T data) {
        return build(200, "success", data);
    }

    public static Response failure() {
        return failure(-1, "failure");
    }

    public static Response failure(final String msg) {
        return failure(-1, msg);
    }

    public static Response failure(final int code, final String msg) {
        return build(code, msg, null);
    }

    public static Response failure(final ErrorCode errorCode) {
        return failure(errorCode.getCode(), errorCode.getMessage());
    }

    public static <T> Response build(final int code, final String msg, final T data) {
        return new Response<T>(code, System.currentTimeMillis(), msg, data);
    }

}