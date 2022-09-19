package com.flance.web.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Objects;

@Component
public class FeignInterceptor implements RequestInterceptor {

    @Resource
    FeignUser feignUser;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (null != feignUser) {
            requestTemplate.header(FeignUser.HEADER_FEIGN_USER, feignUser.getUser());
            requestTemplate.header(FeignUser.HEADER_FEIGN_PASS, feignUser.getPass());
        }
        if (Objects.isNull(attributes)) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String values = request.getHeader(name);
                requestTemplate.header(name, values);
            }
        }
    }
}
