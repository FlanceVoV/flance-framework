package com.flance.web.utils.config;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 全局拦截器配置
 * @author jhf
 */
@Slf4j
@Configuration
public class GlobalInterceptorConfig implements WebMvcConfigurer {

    @Value("${flance.global.interceptor:logInterceptor}")
    private List<String> interceptors;

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (!interceptors.contains("logInterceptor")) {
            interceptors.add("logInterceptor");
        }
        for (String interceptor : interceptors) {
            log.info("加载全局拦截器【{}】", interceptor);
            HandlerInterceptor handlerInterceptor = applicationContext.getBean(interceptor, HandlerInterceptor.class);
            registry.addInterceptor(handlerInterceptor).addPathPatterns("/**");
        }
    }

}
