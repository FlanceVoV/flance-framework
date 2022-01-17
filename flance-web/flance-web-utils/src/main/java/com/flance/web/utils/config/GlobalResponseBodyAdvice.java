package com.flance.web.utils.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.flance.web.utils.UrlMatchUtil;
import com.flance.web.utils.web.response.WebResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

@ControllerAdvice
public class GlobalResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Value("${flance.response.advice::#{null}}")
    private List<String> ignoreUrl;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> clazz) {
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter,
                                  MediaType mediaType, Class<? extends HttpMessageConverter<?>> clazz,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        Object result;

        if (body instanceof WebResponse ||
                ignore(request.getURI().toString())) {
            result = body;
        } else if (body instanceof String) {
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.writeValueAsString(WebResponse.getSucceed(body, "请求成功"));
        } else {
            result = WebResponse.getSucceed(body, "请求成功");
        }

        return result;
    }

    private boolean ignore(String url) {
        return UrlMatchUtil.matchUrl(url, ignoreUrl);
    }

}
