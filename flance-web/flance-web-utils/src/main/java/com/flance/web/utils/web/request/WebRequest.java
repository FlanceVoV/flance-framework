package com.flance.web.utils.web.request;

import lombok.Data;

/**
 * 鉴权请求
 * @author jhf
 */
@Data
public class WebRequest {

    private String url;

    private String requestId;

    private String method;

    private String token;

}
