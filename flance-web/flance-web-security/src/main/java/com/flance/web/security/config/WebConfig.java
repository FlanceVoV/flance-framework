package com.flance.web.security.config;

import com.flance.web.security.intercept.RequestParamIntercept;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web拦截器配置
 * @author jhf
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public RequestParamIntercept webInterceptor() {
        return new RequestParamIntercept();
    }

    /**
     * 参数过滤器配置
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webInterceptor()).addPathPatterns();
    }

}
