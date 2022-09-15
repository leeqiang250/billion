package com.billion.gateway.aptos.kiko.filter;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author liqiang
 */
@Component
public class ReplaceRequestHeaderMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver {

    @Resource
    RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @Resource
    DefaultListableBeanFactory defaultListableBeanFactory;

    public ReplaceRequestHeaderMethodArgumentResolver(ConfigurableBeanFactory beanFactory) {
        super(beanFactory);
    }

    @PostConstruct
    public void init() {
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>(requestMappingHandlerAdapter.getArgumentResolvers());
        for (int i = 0; i < resolvers.size(); i++) {
            if (resolvers.get(i) instanceof RequestHeaderMethodArgumentResolver) {
                resolvers.set(i, new ReplaceRequestHeaderMethodArgumentResolver(defaultListableBeanFactory));
            }
        }
        requestMappingHandlerAdapter.setArgumentResolvers(resolvers);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (parameter.hasParameterAnnotation(RequestHeader.class)
                && !Map.class.isAssignableFrom(parameter.nestedIfOptional().getNestedParameterType()));
    }

    @Override
    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        RequestHeader ann = parameter.getParameterAnnotation(RequestHeader.class);
        Assert.state(Objects.nonNull(ann), "No RequestHeader Annotation");
        return new RequestHeaderNamedValueInfo(ann);
    }

    @Override
    protected @Nullable Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
        String[] headerValues = request.getHeaderValues(name);

        if (headerValues == null) {
            //TODO
            return JSONObject.parseObject("{}", parameter.getParameterType());
            //return null;
        }

        if (String.class != parameter.getParameterType()) {
            String headerValue = headerValues[0];
            if (Objects.isNull(headerValue) || "".equals(headerValue)) {
                return JSONObject.parseObject("{}", parameter.getParameterType());
            }

            return JSONObject.parseObject(URLDecoder.decode(headerValue, "UTF-8"), parameter.getParameterType());
        }

        return (1 == headerValues.length ? headerValues[0] : headerValues);
    }

    @Override
    protected void handleMissingValue(String name, MethodParameter parameter) throws ServletRequestBindingException {
        //throw new MissingRequestHeaderException(name, parameter);
    }

    static final class RequestHeaderNamedValueInfo extends NamedValueInfo {

        RequestHeaderNamedValueInfo(RequestHeader annotation) {
            super(annotation.name(), annotation.required(), annotation.defaultValue());
        }

    }

}
