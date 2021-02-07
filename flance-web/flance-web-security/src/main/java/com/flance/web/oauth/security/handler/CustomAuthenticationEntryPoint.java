package com.flance.web.oauth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flance.web.oauth.security.utils.SecurityConstant;
import com.flance.web.utils.web.response.WebResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 无session访问异常处理
 * @author jhf
 */
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType("application/json;charset=UTF-8");
        WebResponse webResponse = WebResponse.getFailed(SecurityConstant.ERROR_NULL_SESSION, "会话失效！请重新登录");
        response.getWriter().write(objectMapper.writeValueAsString(webResponse));
    }
}
