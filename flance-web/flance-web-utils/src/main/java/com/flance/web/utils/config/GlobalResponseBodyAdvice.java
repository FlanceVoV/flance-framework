package com.flance.web.utils.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.flance.web.utils.GsonUtils;
import com.flance.web.utils.UrlMatchUtil;
import com.flance.web.utils.web.response.WebResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;
import java.util.Map;

@Slf4j
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
        Object result  = null;
        if(mediaType.includes(MediaType.APPLICATION_OCTET_STREAM)){
            return body;
        }
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (body instanceof WebResponse ||
                ignore(request.getURI().toString())) {
            result = body;
        } else if (body instanceof String) {
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.writeValueAsString(WebResponse.getSucceed(body, "请求成功"));
        } else if (body instanceof Map) {
            Object status = ((Map<?, ?>) body).get("status");
            if (null != status && !status.toString().equals("200")) {
                result = WebResponse.getFailed(body, "-1", "请求失败");
            } else {
                result = WebResponse.getSucceed(body, "请求成功");
            }
        } else {
            result = WebResponse.getFailed(body, "-1", "请求失败，响应无法解析");
        }
        log.info("接口响应：" + GsonUtils.toJSONString(result));
        return result;
    }



    private boolean ignore(String url) {
        return UrlMatchUtil.matchUrl(url, ignoreUrl);
    }

}
