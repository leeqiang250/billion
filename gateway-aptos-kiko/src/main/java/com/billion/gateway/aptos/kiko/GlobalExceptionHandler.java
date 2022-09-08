package com.billion.gateway.aptos.kiko;

import com.billion.model.code.BizErrorCode;
import com.billion.model.exception.BizException;
import com.billion.model.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liqiang
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MissingServletRequestParameterException.class, HttpMessageNotReadableException.class})
    public Response handlerMessageNotReadable(@NotNull HttpServletRequest request, Exception ex) {
        log.info("HttpMessageNotReadableException [path={}][msg={}] {}", request.getRequestURI(), ex.getMessage(), ex);
        return Response.failure(BizErrorCode.DATA_VALIDATION_FAILURE);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Response handlerMethodArgumentNotValid(@NotNull HttpServletRequest request, Exception ex) {
        log.info("BindException [path={}][msg={}] {}", request.getRequestURI(), ex.getMessage(), ex);
        return Response.failure(BizErrorCode.DATA_BIND_VALIDATION_FAILURE);
    }

    @ExceptionHandler(BizException.class)
    public Response handleBusinessException(@NotNull HttpServletRequest request, BizException ex) {
        log.info("BizException [path={}][code={}][msg={}] {}", request.getRequestURI(), ex.getCode(), ex.getMessage(), ex);
        return Response.failure(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Response handleException(@NotNull HttpServletRequest request, Exception ex) {
        log.info("Exception [path={}][msg={}] {}", request.getRequestURI(), ex.getMessage(), ex);
        return Response.failure(BizErrorCode.SYSTEM_ERROR);
    }

}