package com.flance.web.oauth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flance.web.oauth.security.exception.AuthException;
import com.flance.web.oauth.security.utils.SecurityConstant;
import com.flance.web.utils.web.response.WebResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理器
 * @author jhf
 */
@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        logger.info("登录失败");
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType("application/json;charset=UTF-8");
        if (exception instanceof AuthException) {
            response.getWriter().write(objectMapper.writeValueAsString(((AuthException) exception).getBaseResponse()));
        } else {
            WebResponse webResponse = WebResponse.getFailed(SecurityConstant.ERROR_UNKNOWN, "登录失败：" + exception.getMessage());
            response.getWriter().write(objectMapper.writeValueAsString(webResponse));
        }
        exception.printStackTrace();
    }

}
