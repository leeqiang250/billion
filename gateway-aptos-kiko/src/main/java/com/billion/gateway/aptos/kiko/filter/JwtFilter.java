package com.billion.gateway.aptos.kiko.filter;

import com.billion.model.request.ResponseElapsed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.billion.model.constant.RequestPathConstant.SLASH;
import static com.billion.model.constant.RequestPathConstant.WHITE;

/**
 * @author liqiang
 */
@Slf4j
@Component
public class JwtFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long ms = System.currentTimeMillis();

        String uri = ((HttpServletRequest) servletRequest).getRequestURI();
        if (SLASH.equals(uri)) {
            filterChain.doFilter(servletRequest, servletResponse);

            requestInfo(uri, ms);
            return;
        }

        boolean result = false;
        for (int i = 0; i < WHITE.length; i++) {
            if (StringUtils.startsWithIgnoreCase(uri, WHITE[i])) {
                result = true;
                break;
            }
        }

        if (result) {
            filterChain.doFilter(servletRequest, servletResponse);

            requestInfo(uri, ms);
            return;
        }

        //TODO 稍后放开
        //((HttpServletResponse) servletResponse).sendRedirect(V1_ERROR);
        filterChain.doFilter(servletRequest, servletResponse);

        requestInfo(uri, ms);
    }

    void requestInfo(String uri, long ms) {
        log.info("{}", ResponseElapsed.builder()
                .uri(uri)
                .elapsed(System.currentTimeMillis() - ms)
                .build());
    }

}