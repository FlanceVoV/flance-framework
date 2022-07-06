package com.flance.components.form.infrastructure.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * url过滤拦截器
 * @author jhf
 */
@Component
public class UrlIgnoreInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(UrlIgnoreInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) {
        log.info("[flance-comp-form]跳过请求，getRequestURI:[{}]", request.getRequestURI());
        log.info("[flance-comp-form]跳过请求，getRequestURL:[{}]", request.getRequestURL());
        return false;
    }

}
