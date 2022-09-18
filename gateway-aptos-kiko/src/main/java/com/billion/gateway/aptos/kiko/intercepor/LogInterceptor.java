package com.billion.gateway.aptos.kiko.intercepor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liqiang
 */
@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("------------------------------------------------------------------------------------------------");
        log.info("[request begin] uri:[{}]", request.getRequestURI());
        request.setAttribute("k-ts", System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        log.info("[request end] uri:[{}] elapsed:[{} ms]", request.getRequestURI(), System.currentTimeMillis() - Long.parseLong(request.getAttribute("k-ts").toString()));
        log.info("------------------------------------------------------------------------------------------------");
    }

}