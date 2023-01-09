package com.flance.web.gateway.filter;

import com.flance.web.gateway.common.GatewayBodyEnum;
import com.flance.web.gateway.decorator.RsaResponseDecorator;
import com.flance.web.utils.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 无加密filter
 * @author jhf
 */
@Slf4j
@Component
public class NonEncodeLogFilter implements GatewayFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();
        RsaResponseDecorator rsaResponseDecorator = new RsaResponseDecorator(response, null, RequestUtil.getLogId(), GatewayBodyEnum.NON_DECODE);
        return chain.filter(exchange.mutate().response(rsaResponseDecorator).build()).doFinally(obj -> RequestUtil.remove());

    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 3;
    }
}
