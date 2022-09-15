package com.billion.model.exception;

import com.billion.model.code.ErrorCode;
import lombok.Data;

/**
 * @author liqiang
 */
@Data
public class BizException extends RuntimeException {

    int code;

    String message;

    public BizException() {
        super();
    }

    public BizException(String message) {
        super(message);
        this.message = message;
    }

    public BizException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public BizException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BizException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public BizException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

}