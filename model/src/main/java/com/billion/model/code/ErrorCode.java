package com.billion.model.code;

/**
 * @author liqiang
 */
public interface ErrorCode {

    /**
     * 错误代号
     */
    int getCode();

    /**
     * 具体信息
     */
    String getMessage();
}
