package com.flance.web.utils;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class AssertException extends RuntimeException {

    private String msg;

    private String code;

    private String methodName;

    private String className;

    public enum ErrCode {

        SYS_ERROR("系统错误：未知原因", "-1"),

        ;


        private String msg;

        private String code;

        ErrCode(String msg, String code) {

        };

    }

    public static AssertException getNormal(String msg, String code) {
        return AssertException.builder()
                .msg(msg)
                .code(code)
                .build();
    }

    public static AssertException getByEnum(ErrCode errCode) {
        return AssertException.builder()
                .msg(errCode.msg)
                .code(errCode.code)
                .build();
    }

}
