package com.billion.gateway.aptos.kiko.filter;

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
@Component
public class JwtFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String uri = ((HttpServletRequest) servletRequest).getRequestURI();
        if (SLASH.equals(uri)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        boolean result = false;
        for (int i = 0; i < WHITE.length; i++) {
            System.out.println(WHITE[i]);
            if (StringUtils.startsWithIgnoreCase(uri, WHITE[i])) {
                result = true;
                break;
            }
        }

        if (result) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        //TODO 稍后放开
        //((HttpServletResponse) servletResponse).sendRedirect(V1_ERROR);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}