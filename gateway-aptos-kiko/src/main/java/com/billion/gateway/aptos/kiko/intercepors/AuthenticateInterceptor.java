package com.billion.gateway.aptos.kiko.intercepors;

import com.billion.model.enums.Authenticate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static com.billion.model.constant.RequestPathError.FORBID;

/**
 * @author liqiang
 */
@Slf4j
@Component
public class AuthenticateInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        boolean result = true;
        if (handler instanceof HandlerMethod) {
            Authenticate authenticate = ((HandlerMethod) handler).getMethodAnnotation(Authenticate.class);
            if (Objects.isNull(authenticate)) {
                return true;
            }

            switch (authenticate.identify()) {
                case PUBLIC: {
                    result = true;
                    break;
                }
                case PRIVATE: {
                    result = false;
                    break;
                }
                case FORBID: {
                    result = false;
                    response.sendRedirect(FORBID);
                    break;
                }
                default: {
                    result = false;
                }
            }
        }

        return result;
    }

}