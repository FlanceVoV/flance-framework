package com.flance.jdbc.jpa.simple.common.request;

/**
 * 响应构造
 * @author jhf
 */
@Deprecated
public class ResponseBuilder {

    private static final String SUCCESS_CODE = "00000";
    private static final String FAIL_CODE = "-1";
    private static final Boolean SUCCESS_FLAG = true;
    private static final Boolean FAIL_FLAG = false;

    public static WebResponse getSuccess(WebResponse webResponse) {
        webResponse.setSuccess(SUCCESS_FLAG);
        webResponse.setCode(SUCCESS_CODE);
        return webResponse;
    }

    public static WebResponse getFail(WebResponse webResponse) {
        webResponse.setSuccess(FAIL_FLAG);
        webResponse.setCode(FAIL_CODE);
        return webResponse;
    }

}
