package com.flance.web.gateway.decorator;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;


public class RsaRequestDecorator extends ServerHttpRequestDecorator {

    private final String logId;

    public RsaRequestDecorator(ServerHttpRequest delegate, String logId) {
        super(delegate);
        this.logId = logId;
    }

    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.putAll(super.getHeaders());
        headerSetting(httpHeaders);
        return httpHeaders;
    }

    private void headerSetting(HttpHeaders httpHeaders) {
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.CONTENT_ENCODING, "UTF-8");
        httpHeaders.remove(HttpHeaders.TRANSFER_ENCODING);
        httpHeaders.remove(HttpHeaders.TRANSFER_ENCODING);
    }
}
