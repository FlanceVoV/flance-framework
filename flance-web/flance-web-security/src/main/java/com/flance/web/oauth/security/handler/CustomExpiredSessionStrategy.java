package com.flance.web.oauth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flance.web.security.common.SecurityConstant;
import com.flance.web.utils.web.response.WebResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import java.io.IOException;

/**
 * 多端登录顶号
 * @author jhf
 */
@Slf4j
@Component
public class CustomExpiredSessionStrategy implements SessionInformationExpiredStrategy {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        log.info("多端登录！");
        event.getResponse().setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        event.getResponse().setContentType("application/json;charset=UTF-8");
        WebResponse webResponse = WebResponse.getFailed(SecurityConstant.ERROR_MULTI_LOGIN, "该账号已在其他地方登录！如非本人操作，请尝试重新登录并修改密码");
        event.getResponse().getWriter().write(objectMapper.writeValueAsString(webResponse));
    }
}
