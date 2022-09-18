package com.billion.gateway.aptos.kiko.filter;

import com.billion.model.request.ResponseElapsed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author liqiang
 */
@Slf4j
@Component
public class StatisticsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long ms = System.currentTimeMillis();

        filterChain.doFilter(request, response);
        log.info("{}", ResponseElapsed.builder()
                .elapsed(System.currentTimeMillis() - ms)
                .build());
    }

}