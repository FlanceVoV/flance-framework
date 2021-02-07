package com.flance.web.oauth.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

@JsonSerialize(using = AuthExceptionSerializer.class)
public class AuthException extends OAuth2Exception {

    private Integer status = 400;

    public AuthException(String message, Throwable t) {
        super(message, t);
        status = ((OAuth2Exception)t).getHttpErrorCode();
    }

    public AuthException(String message) {
        super(message);
    }

    @Override
    public int getHttpErrorCode() {
        return status;
    }

}
