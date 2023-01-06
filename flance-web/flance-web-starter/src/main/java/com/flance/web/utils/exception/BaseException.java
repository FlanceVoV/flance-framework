package com.flance.web.utils.exception;

import com.flance.web.utils.AssertException;
import com.flance.web.utils.web.response.WebResponse;

public class BaseException extends RuntimeException {

    protected String msg;

    protected String code;

    public BaseException() {

    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BaseException(String msg, String code) {
        this.msg = msg;
        this.code = code;
    }

    public static AssertException getNormal(String msg, String code) {
        return AssertException.builder()
                .msg(msg)
                .code(code)
                .build();
    }

    public WebResponse getResponse() {
        return WebResponse.getFailed(code, msg);
    }

}
