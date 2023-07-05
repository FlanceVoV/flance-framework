package com.flance.web.utils.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.flance.web.utils.GsonUtils;
import com.flance.web.utils.UrlMatchUtil;
import com.flance.web.utils.exception.BaseException;
import com.flance.web.utils.web.response.WebResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
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
                try {
                    log.info("自定义异常转换 -> [{}]", request.getClass());
                    ServletServerHttpRequest sReq = (ServletServerHttpRequest) request;
                    log.info("自定义异常转换 转换 ServletServerHttpRequest -> [{}]", sReq.getClass());
                    log.info("自定义异常转换 解析 ErrorAttributes -> [{}]", sReq.getServletRequest().getAttribute("org.springframework.boot.web.servlet.error.ErrorAttributes.error").getClass());
                    BaseException baseException = (BaseException) sReq.getServletRequest().getAttribute("org.springframework.boot.web.servlet.error.ErrorAttributes.error");
                    log.info("自定义异常转换 转换 BaseException -> [{}]", baseException.getClass());
                    result = baseException.getResponse();
                    log.info("自定义异常转换 完成 result -> [{}]", GsonUtils.toJSONString(result));
                } catch (Exception e) {
                    log.error("自定义异常转换 失败 err -> [{}]", e.getMessage());
                    result = WebResponse.getFailedDebug(body, "-1", "请求失败", e.getMessage());
                }
            } else {
                result = WebResponse.getSucceed(body, "请求成功");
            }
        } else {
            try {
                result = WebResponse.getSucceed(body, "请求成功");
            } catch (Exception e) {
                e.printStackTrace();
                result = WebResponse.getFailed(body, "-1", "请求失败[" + e.getMessage() + "]");
            }

        }
        String resultStr = GsonUtils.toJSONString(result);
        if (resultStr.length() < 1000) {
            log.info("接口响应：" + resultStr);
        } else {
            log.info("接口响应：" + resultStr.substring(0, 1000) + "...");
            log.debug("接口响应：" + resultStr);
        }
        return result;
    }



    private boolean ignore(String url) {
        return UrlMatchUtil.matchUrl(url, ignoreUrl);
    }

}
