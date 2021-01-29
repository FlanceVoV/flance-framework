package com.flance.web.security.config;

import com.flance.web.security.intercept.BaseParamIntercept;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * web拦截器配置
 * @author jhf
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    BaseParamIntercept baseParamIntercept;

    /**
     * 参数过滤器配置
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(baseParamIntercept).addPathPatterns();
    }

}
