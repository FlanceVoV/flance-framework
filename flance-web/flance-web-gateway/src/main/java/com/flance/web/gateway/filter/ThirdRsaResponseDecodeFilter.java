package com.flance.web.gateway.filter;

import com.flance.web.gateway.common.GatewayBodyEnum;
import com.flance.web.gateway.decorator.RsaResponseDecorator;
import com.flance.web.gateway.service.AppService;
import com.flance.web.gateway.service.RouteApiService;
import com.flance.web.utils.RequestConstant;
import com.flance.web.utils.RequestUtil;
import com.flance.web.utils.route.AppModel;
import com.flance.web.utils.route.RouteApiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * 第三方响应rsa解密
 * @author jhf
 */
@Slf4j
@Component
public class ThirdRsaResponseDecodeFilter implements GatewayFilter, Ordered {

    @Resource
    AppService appService;

    @Resource
    RouteApiService routeApiService;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 5;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String uri = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethodValue();
        String requestId = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_REQUEST_ID);
        String appId = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_APP_ID);
        String headerLogId = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_LOG_ID);
        String version =  exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_REQUEST_VERSION);
        RequestUtil.getLogId(headerLogId);
        if (StringUtils.isEmpty(appId)) {
            log.error("third-response appId为空，无法进行参数解密【method:{}】【uri:{}】【api_id:{}】", method, uri, requestId);
            return Mono.error(new NotFoundException("appId为空"));
        }

        RouteApiModel routeApiModel = routeApiService.getOneByApiIdAndVersion(requestId, version);
        log.info("third-response app_id 【app_id:{}】 => 【app_id:{}】", appId, routeApiModel.getAppId());
        AppModel appModel = appService.getAppByAppId(routeApiModel.getAppId());

        if (null == appModel) {
            log.error("third-response 找不到app【{}】，请确认是否启用【method:{}】【uri:{}】【api_id:{}】", appId, method, uri, requestId);
            return Mono.error(new NotFoundException("third-response 找不到app【" + appId + "】"));
        }

        ServerHttpResponse response = exchange.getResponse();
        RsaResponseDecorator rsaResponseDecorator = new RsaResponseDecorator(response, appModel, RequestUtil.getLogId(), GatewayBodyEnum.RSA_DECODE);
        return chain.filter(exchange.mutate().response(rsaResponseDecorator).build()).doFinally(obj -> RequestUtil.remove());

    }


}
