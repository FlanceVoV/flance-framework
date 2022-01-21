package com.flance.web.gateway.decorator;

import com.flance.web.utils.web.request.WebRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;


public class RsaRequestDecorator extends ServerHttpRequestDecorator {

    public RsaRequestDecorator(ServerHttpRequest delegate) {
        super(delegate);
    }

    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.putAll(super.getHeaders());
        //由于修改了请求体的body，导致content-length长度不确定，因此使用分块编码
        httpHeaders.remove(HttpHeaders.CONTENT_LENGTH);
        httpHeaders.remove(HttpHeaders.TRANSFER_ENCODING);
        return httpHeaders;
    }
}
