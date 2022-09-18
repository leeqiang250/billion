package com.billion.gateway.aptos.kiko.intercepors;

import com.billion.model.enums.Authenticate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author liqiang
 */
@Slf4j
@Component
public class AuthenticateInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            Authenticate authenticate = ((HandlerMethod) handler).getMethodAnnotation(Authenticate.class);
            if (Objects.isNull(authenticate)) {
                return true;
            }

            //String[] identify = authenticate.identify();
            //response.sendRedirect(V1_ERROR);
        }

        return true;
    }

}