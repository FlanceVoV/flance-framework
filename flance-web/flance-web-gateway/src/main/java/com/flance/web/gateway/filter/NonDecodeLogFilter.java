package com.flance.web.gateway.filter;

import com.flance.web.gateway.common.GatewayBodyEnum;
import com.flance.web.gateway.decorator.RsaRequestDecorator;
import com.flance.web.gateway.utils.RsaBodyUtils;
import com.flance.web.utils.RequestConstant;
import com.flance.web.utils.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 无加密filter
 * @author jhf
 */
@Slf4j
@Component
public class NonDecodeLogFilter implements GatewayFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String uri = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethodValue();
        String requestId = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_REQUEST_ID);
        String appId = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_APP_ID);
        String headerLogId = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_LOG_ID);
        RequestUtil.getLogId(headerLogId);
        Mono<Void> mono = RsaBodyUtils.readBody(exchange, chain, null, GatewayBodyEnum.NON_DECODE);
        log.info("入参-解析 【app_id:{}】【api_id:{}】【method:{}】【uri:{}】", appId, requestId, method, uri);
        return mono.doFinally(obj -> RequestUtil.remove());
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 2;
    }
}
