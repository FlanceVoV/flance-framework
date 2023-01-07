package com.flance.web.utils.exception;

import com.flance.web.utils.web.response.WebResponse;

public class AuthException extends BaseException {

    public AuthException(String msg, String code) {
        super(msg, code);
        this.msg = msg;
        this.code = code;
    }

}
