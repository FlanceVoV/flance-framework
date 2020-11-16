package com.flance.web.gateway.handler;

import com.alibaba.fastjson.JSONObject;
import com.flance.web.utils.feign.response.FeignResponse;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * webflux response
 * @author jhf
 */
public class ResponseHandler {

    public static Mono<Void> setResponse(FeignResponse retFeignResponse, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        String message = JSONObject.toJSONString(retFeignResponse);
        byte[] bits = message.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        // 指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

}
