package com.flance.web.gateway.filter;

import com.flance.web.gateway.service.RouteApiService;
import com.flance.web.gateway.service.RouteService;
import com.flance.web.utils.*;
import com.flance.web.utils.route.RouteApiModel;
import com.flance.web.utils.route.RouteModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import jakarta.annotation.Resource;

import static com.flance.web.utils.AssertException.ErrCode.*;

/**
 * 内置服务限流器
 * 任意appId -> 调用某个服务 的频率限制
 * @author jhf
 */
@Slf4j
@Component
public class LimitRouterFilter implements GatewayFilter, Ordered {

    @Resource
    private RouteService routeService;

    @Resource
    private RouteApiService routeApiService;

    @Resource
    private RedisUtils redisUtils;

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String uri = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethodValue();
        String requestId = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_REQUEST_ID);
        String appId = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_APP_ID);
        String headerLogId = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_LOG_ID);
        RequestUtil.getLogId(headerLogId);
        if (StringUtils.isEmpty(appId)) {
            log.error("appId为空，无法进行route限流【method:{}】【uri:{}】【api_id:{}】", method, uri, requestId);
            return Mono.error(new NotFoundException("appId为空"));
        }

        RouteApiModel apiModel = routeApiService.getOneByApiIdAndVersion(appId, null);
        if (null == apiModel) {
            log.error("找不到api【{}】，请确认是否启用【method:{}】【uri:{}】【api_id:{}】", appId, method, uri, requestId);
            return Mono.error(new NotFoundException("找不到api【" + appId + "】"));
        }

        RouteModel routeModel = routeService.getRouteModelById(apiModel.getRouteId());
        if (null == routeModel) {
            log.error("找不到route【{}】，请确认是否启用【method:{}】【uri:{}】【api_id:{}】", appId, method, uri, requestId);
            return Mono.error(new NotFoundException("找不到route【" + apiModel.getRouteId() + "】"));
        }

        log.info("开始服务限流");
        Integer limit = routeModel.getRequestLimit();
        if (null != limit && limit > 0) {
            String limitLock = appId + "_" + routeModel.getRouteId();
            if (null == redisUtils.get(RequestConstant.LIMIT_ROUTE_KEY + ":" + limitLock)) {
                redisUtils.add(RequestConstant.LIMIT_ROUTE_KEY + ":" + limitLock, "1", limit.longValue());
            } else {
                AssertUtil.throwError(AssertException.getByEnum(SYS_GATEWAY_LIMIT_ROUTER_ERROR));
            }
        } else {
            log.info("服务[{}]未开启限流", routeModel.getRouteId());
        }

        return chain.filter(exchange);
    }


}
