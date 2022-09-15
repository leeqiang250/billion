package com.billion.gateway.aptos.kiko.filter;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.billion.model.constant.RequestPathConstant.V1_ERROR;
import static com.billion.model.constant.RequestPathConstant.WHITE;

/**
 * @author liqiang
 */
@Component
public class JwtFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String uri = ((HttpServletRequest) servletRequest).getRequestURI();
        boolean result = false;
        for (int i = 0; i < WHITE.length; i++) {
            if (StringUtils.startsWithIgnoreCase(uri, WHITE[i])) {
                result = true;
                break;
            }
        }

        if (result) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        ((HttpServletResponse) servletResponse).sendRedirect(V1_ERROR);
    }
}