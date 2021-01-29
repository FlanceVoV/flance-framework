package com.flance.web.utils.feign.request;

/**
 * 请求
 * 推荐所有接口统一使用WebRequest
 * @author jhf
 */
@Deprecated
public class FeignRequest {

    private String url;

    private String requestId;

    private String method;

    private String token;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
