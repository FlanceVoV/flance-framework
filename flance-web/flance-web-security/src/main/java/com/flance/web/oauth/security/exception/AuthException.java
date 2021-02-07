package com.flance.web.oauth.security.exception;

import com.flance.web.utils.web.response.WebResponse;
import org.springframework.security.authentication.BadCredentialsException;


/**
 * 认证信息异常封装
 * @author jhf
 */
public class AuthException extends BadCredentialsException {

    private WebResponse baseResponse;

    private String code;

    private String msg;

    public AuthException(){
        super("ERROR_UNKNOWN_TOKEN_READ_EXCEPTION");
    }

    public AuthException(String msg, String code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public AuthException setBaseResponse(WebResponse baseResponse) {
        this.baseResponse = baseResponse;
        return this;
    }

    public WebResponse getBaseResponse() {
        if  (null == baseResponse) {
            return WebResponse.getFailed(code, msg);
        }
        return this.baseResponse;
    }
}
