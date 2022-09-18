package com.billion.gateway.aptos.kiko.intercepors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author liqiang
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Resource
    AuthenticateInterceptor authenticateInterceptor;

    @Resource
    LogInterceptor logInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor).addPathPatterns("/**");
        registry.addInterceptor(authenticateInterceptor).addPathPatterns("/**");
    }

}