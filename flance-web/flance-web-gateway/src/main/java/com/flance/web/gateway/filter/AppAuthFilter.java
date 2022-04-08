package com.flance.web.gateway.filter;

import com.flance.web.gateway.service.AppService;
import com.flance.web.gateway.service.RouteApiService;
import com.flance.web.utils.RequestConstant;
import com.flance.web.utils.RequestUtil;
import com.flance.web.utils.route.AppModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * app权限校验过滤器
 * @author jhf
 */
@Slf4j
@Component
public class AppAuthFilter implements GatewayFilter, Ordered {

    @Resource
    private AppService appService;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String uri = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethodValue();
        String apiId = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_REQUEST_ID);
        String appId = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_APP_ID);
        String headerLogId = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_LOG_ID);
        RequestUtil.getLogId(headerLogId);
        if (StringUtils.isEmpty(appId)) {
            log.error("appId为空，无法进行权限校验【method:{}】【uri:{}】【api_id:{}】", method, uri, apiId);
            return Mono.error(new NotFoundException("appId为空"));
        }

        if (StringUtils.isEmpty(apiId)) {
            log.error("请求接口编号为空，无法进行权限校验【appId:{}】【method:{}】【uri:{}】", appId, method, uri);
            return Mono.error(new NotFoundException("请求接口编号为空"));
        }

        AppModel appModel = appService.getAppByAppId(appId);
        if (null == appModel) {
            log.error("appId为空，无法进行权限校验【method:{}】【uri:{}】【api_id:{}】", method, uri, apiId);
            return Mono.error(new NotFoundException("找不到app[" + appId + "]"));
        }

        if (null == appModel.getApiResources() || appModel.getApiResources().size() == 0) {
            log.error("没有配置权限【appId:{}】【method:{}】【uri:{}】【api_id:{}】", appId, method, uri, apiId);
            return Mono.error(new NotFoundException("没有配置权限[" + appId + "][" + apiId + "]"));
        }

        if (!appModel.getApiResources().contains(apiId)) {
            log.error("没有权限访问【appId:{}】【method:{}】【uri:{}】【api_id:{}】", appId, method, uri, apiId);
            return Mono.error(new NotFoundException("没有权限[" + appId + "][" + apiId + "]"));
        }

        return chain.filter(exchange);
    }


}
