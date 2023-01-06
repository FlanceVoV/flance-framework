package com.flance.web.gateway.filter;

import com.flance.web.gateway.common.BizConstant;
import com.flance.web.gateway.service.RouteApiService;
import com.flance.web.utils.RedisUtils;
import com.flance.web.utils.RequestConstant;
import com.flance.web.utils.RequestUtil;
import com.flance.web.utils.route.AppApiLimitModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerErrorException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * app - api 限流器
 * @author jhf
 */
@Slf4j
@Component
public class LimitAppApiFilter implements GatewayFilter, Ordered {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private RouteApiService routeApiService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String uri = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethodValue();
        String apiId = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_REQUEST_ID);
        String appId = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_APP_ID);
        String headerLogId = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_LOG_ID);
        RequestUtil.getLogId(headerLogId);
        // 校验接口编号
        if (StringUtils.isEmpty(apiId)) {
            apiId =  exchange.getRequest().getQueryParams().getFirst(RequestConstant.HEADER_REQUEST_ID);
        }
        if (StringUtils.isEmpty(apiId)) {
            log.error("请求接口编号为空，无法进行app - api 限流【appId:{}】【method:{}】【uri:{}】", appId, method, uri);
            return Mono.error(new NotFoundException("请求接口编号为空"));
        }
        if (StringUtils.isEmpty(appId)) {
            log.error("appId为空，无法进行app - api 限流【method:{}】【uri:{}】【api_id:{}】", method, uri, apiId);
            return Mono.error(new NotFoundException("appId为空"));
        }
        boolean flag = false;
        AppApiLimitModel appApiLimitModel = routeApiService.getOneByAppIdAndApiId(appId, apiId);
        if (null == appApiLimitModel) {
            log.warn("找不到api【{}】，请确认是否启用【method:{}】【uri:{}】【api_id:{}】", appId, method, uri, apiId);
        } else {
            String key = "limit_app_api_" + appId + "_" + apiId;
            if (null != appApiLimitModel.getLimitCount() && null != appApiLimitModel.getLimitSecond()) {
                flag = redisUtils.limit(key, appApiLimitModel.getLimitCount(), appApiLimitModel.getLimitSecond());
            }
        }
        if (flag) {
            return Mono.error(new NotFoundException("接口被限流了！"));
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 3;
    }
}
