package com.flance.web.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flance.web.security.utils.SecurityConstant;
import com.flance.web.utils.web.response.WebResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义403返回
 * @author jhf
 */
@Slf4j
@Component
public class AccessFailureHandler implements AccessDeniedHandler {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException, ServletException {
        log.info("鉴权失败！");
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(WebResponse.getFailed(SecurityConstant.ERROR_NULL_AUTH,"鉴权失败！")));
    }
}
