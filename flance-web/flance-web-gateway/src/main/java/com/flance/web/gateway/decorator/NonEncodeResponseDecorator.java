package com.flance.web.gateway.decorator;

import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;

public class NonEncodeResponseDecorator extends ServerHttpResponseDecorator {

    public NonEncodeResponseDecorator(ServerHttpResponse delegate) {
        super(delegate);
    }
}
