package com.flance.components.form.infrastructure.config;

import com.flance.components.form.infrastructure.interceptor.UrlIgnoreInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Properties;

/**
 * 拦截器配置器
 * @author jhf
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${flance.comp.form.ignore}")
    private String[] ignoreUrls;

    @Resource
    private UrlIgnoreInterceptor urlIgnoreInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (null != ignoreUrls && ignoreUrls.length > 0) {
            Arrays.asList(ignoreUrls).forEach(url -> {
                // 自定义拦截器，添加拦截路径和排除拦截路径
                registry.addInterceptor(urlIgnoreInterceptor).addPathPatterns(url);
            });
        }
    }

}
