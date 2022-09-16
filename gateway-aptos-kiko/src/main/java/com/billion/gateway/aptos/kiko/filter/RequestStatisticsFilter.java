package com.billion.gateway.aptos.kiko.filter;

import com.billion.model.request.ResponseElapsed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author liqiang
 */
@Slf4j
//@Component
public class RequestStatisticsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long ms = System.currentTimeMillis();
        filterChain.doFilter(servletRequest, servletResponse);
        String uri = ((HttpServletRequest) servletRequest).getRequestURI();
        ResponseElapsed responseElapsed = ResponseElapsed.builder()
                .uri(uri)
                .elapsed(System.currentTimeMillis() - ms)
                .build();
        log.info("{}", responseElapsed);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

}