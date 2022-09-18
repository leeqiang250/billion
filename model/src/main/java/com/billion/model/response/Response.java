package com.billion.model.response;

import com.billion.model.enums.BizErrorCode;
import lombok.*;

import java.io.Serializable;

/**
 * @author liqiang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> implements Serializable {

    int code;

    long ts;

    String msg;

    T data;

    public static <T> Response success(final T data) {
        return build(200, "success", data);
    }

    public static Response failure(final String msg) {
        return failure(-1, msg);
    }

    public static Response failure(final int code, final String msg) {
        return build(code, msg, null);
    }

    public static Response failure(final BizErrorCode bizErrorCode) {
        return failure(bizErrorCode.getCode(), bizErrorCode.getMessage());
    }

    public static <T> Response build(final int code, final String msg, final T data) {
        return new Response<T>(code, System.currentTimeMillis(), msg, data);
    }

    public final static Response SUCCESS = Response.success(null);

    public final static Response FAILURE = Response.failure(-1, "failure");

    public final static Response INVALID = Response.failure(-2, "invalid");

}