package com.flance.web.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Resource
    RedisUtils redisUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(RequestConstant.HEADER_TOKEN);
        if (!StringUtils.isEmpty(token)) {
            redisUtils.setExp(RequestConstant.SYS_TOKEN_KEY + token, 7200L);
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
