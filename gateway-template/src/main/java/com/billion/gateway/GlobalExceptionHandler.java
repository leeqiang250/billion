package com.billion.gateway;

import com.billion.model.code.BizErrorCode;
import com.billion.model.exception.BizException;
import com.billion.model.response.Response;
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
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MissingServletRequestParameterException.class, HttpMessageNotReadableException.class})
    public Response handlerMessageNotReadable(@NotNull HttpServletRequest request, Exception ex) {
        System.out.println(String.format("HttpMessageNotReadableException [path={}][msg={}] {}", request.getRequestURI(), ex.getMessage(), ex));
        return Response.failure(BizErrorCode.DATA_VALIDATION_FAILURE);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Response handlerMethodArgumentNotValid(@NotNull HttpServletRequest request, Exception ex) {
        System.out.println(String.format("BindException [path={}][msg={}] {}", request.getRequestURI(), ex.getMessage(), ex));
        return Response.failure(BizErrorCode.DATA_BIND_VALIDATION_FAILURE);
    }

    @ExceptionHandler(BizException.class)
    public Response handleBusinessException(@NotNull HttpServletRequest request, BizException ex) {
        System.out.println(String.format("BizException [path={}][code={}][msg={}] {}", request.getRequestURI(), ex.getCode(), ex.getMessage(), ex));
        return Response.failure(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Response handleException(@NotNull HttpServletRequest request, Exception ex) {
        System.out.println(String.format("Exception [path={}][msg={}] {}", request.getRequestURI(), ex.getMessage(), ex));
        return Response.failure(BizErrorCode.SYSTEM_ERROR);
    }

}