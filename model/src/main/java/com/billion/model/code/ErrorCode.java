package com.billion.model.code;

/**
 * @author liqiang
 */
public interface ErrorCode {

    /**
     * 错误码
     * @return 错误码
     */
    int getCode();

    /**
     * 错误描述
     * @return 错误描述
     */
    String getMessage();
}
