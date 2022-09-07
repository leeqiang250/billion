package com.billion.gateway.aptos.kiko;

import com.billion.model.request.ResponseElapsed;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author liqiang
 */
@Component
public class FilterResponseElapsed implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long ms = System.currentTimeMillis();
        filterChain.doFilter(servletRequest, servletResponse);
        String uri = ((HttpServletRequest) servletRequest).getRequestURI();
        ResponseElapsed responseElapsed = ResponseElapsed.builder().uri(uri).elapsed(System.currentTimeMillis() - ms).build();
        //TODO
        System.out.println("----------");
        System.out.println(responseElapsed.toString());
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

}