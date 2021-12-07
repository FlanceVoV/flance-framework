package com.flance.web.utils.config;


import com.flance.web.utils.UrlMatchUtil;
import com.flance.web.utils.web.response.WebResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

public class GobalResponseBodyAdvice implements ResponseBodyAdvice {

    private List<String> ignoreUrl;

    public GobalResponseBodyAdvice() {

    }

    public GobalResponseBodyAdvice(List<String> ignoreUrl) {
        this.ignoreUrl = ignoreUrl;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter,
                                  MediaType mediaType, Class aClass,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        if (body instanceof WebResponse ||
                ignore(request.getURI().toString())) {
            return body;
        }

        return WebResponse.getSucceed(body, "请求成功");
    }

    private boolean ignore(String url) {
        if (null == ignoreUrl) {
            return false;
        }
        return UrlMatchUtil.matchUrl(url, ignoreUrl);
    }

}
