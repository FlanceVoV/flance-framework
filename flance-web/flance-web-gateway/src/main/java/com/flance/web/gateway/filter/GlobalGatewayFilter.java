package com.flance.web.gateway.filter;

import com.flance.web.gateway.service.GatewayService;
import com.flance.web.utils.RequestConstant;
import com.flance.web.utils.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.UUID;

@Slf4j
@Component
public class GlobalGatewayFilter implements GlobalFilter, Ordered {

    @Resource
    GatewayService gatewayService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String uri = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethodValue();
        String requestId =  exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_REQUEST_ID);
        String logId = UUID.randomUUID().toString();
        RequestUtil.setLogId(logId);
        log.info("gateway-global-filter：请求标识[{}]，请求路径[({}){}]，url标识[{}]", logId, method, uri, requestId);
        final String token = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_TOKEN);

        // 设置下游传递参数
        exchange.getRequest().mutate()
                .headers(header -> {
                    if (null != token) {
                        header.set(RequestConstant.HEADER_TOKEN, token);
                    }
                    header.set(RequestConstant.HEADER_LOG_ID, logId);
                }).build();

        return gatewayService.filter(exchange, chain).doFinally(obj -> RequestUtil.remove());
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
