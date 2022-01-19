package com.flance.web.gateway.service;


import com.flance.web.utils.web.response.WebResponse;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义业务接口
 * @author jhf
 */
public interface GatewayService {

    /**
     * 自定义业务权限过滤的业务逻辑
     * @param exchange  参数
     * @param chain     调用链
     * @param webResponse  鉴权响应
     * @return          返回
     */
    @Deprecated
    Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain, WebResponse webResponse);

    /**
     * 自定义网关业务逻辑处理
     * @param exchange  参数
     * @param chain     调用连
     * @return          返回
     */
    Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain);

}
