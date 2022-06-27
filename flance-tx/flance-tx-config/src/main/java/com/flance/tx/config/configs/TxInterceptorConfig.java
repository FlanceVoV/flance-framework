package com.flance.tx.config.configs;

import com.flance.tx.config.tx.FlanceTxRequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class TxInterceptorConfig implements WebMvcConfigurer {

    @Resource
    private FlanceTxRequestInterceptor flanceTxRequestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(flanceTxRequestInterceptor).addPathPatterns("/**");
    }

}
