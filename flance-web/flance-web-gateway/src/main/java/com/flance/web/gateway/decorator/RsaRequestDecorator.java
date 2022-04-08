package com.flance.web.gateway.decorator;

import com.flance.web.utils.web.request.GatewayRequest;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;


public class RsaRequestDecorator extends ServerHttpRequestDecorator {

    private final String logId;

    private final CachedBodyOutputMessage outputMessage;


    public RsaRequestDecorator(ServerHttpRequest delegate, String logId, CachedBodyOutputMessage outputMessage) {
        super(delegate);
        this.logId = logId;
        this.outputMessage = outputMessage;
    }

    @Override
    public Flux<DataBuffer> getBody() {
        return outputMessage.getBody();
    }

    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.putAll(super.getHeaders());
        headerSetting(httpHeaders);
        return httpHeaders;
    }

    private void headerSetting(HttpHeaders httpHeaders) {
//        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.CONTENT_ENCODING, "UTF-8");
        httpHeaders.remove(HttpHeaders.TRANSFER_ENCODING);
        httpHeaders.remove(HttpHeaders.CONTENT_LENGTH);
    }
}
