package com.flance.web.utils.web.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WebResponse {

    private Boolean success;

    private String msg;

    private String code;

    private Object data;

    public static WebResponse getSucceed(Object data, String msg) {
        return WebResponse.builder().code("000000").success(true).msg(msg).data(data).build();
    }

    public static WebResponse getFailed(String code, String msg) {
        return WebResponse.builder().code(code).success(false).msg(msg).build();
    }

}
