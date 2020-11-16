package com.flance.web.common.utils;

import com.flance.web.common.request.WebResponse;

/**
 * 响应构造
 * @author jhf
 */
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
