package com.flance.web.oauth.security.config;

import com.flance.web.oauth.security.intercept.BaseParamIntercept;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.Resource;

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
