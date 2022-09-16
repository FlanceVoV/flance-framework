package com.flance.web.nacos;

public class FlanceNacosException extends RuntimeException {

    private String msg;

    private String code;

    public FlanceNacosException() {

    }

    public FlanceNacosException(String code, String msg) {
        this.msg = msg;
        this.code = code;
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

}
