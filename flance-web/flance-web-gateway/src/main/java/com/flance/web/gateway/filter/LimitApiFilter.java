package com.flance.web.gateway.filter;

import com.flance.web.gateway.service.RouteApiService;
import com.flance.web.gateway.service.RouteService;
import com.flance.web.utils.AssertException;
import com.flance.web.utils.AssertUtil;
import com.flance.web.utils.RedisUtils;
import com.flance.web.utils.RequestConstant;
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

import javax.annotation.Resource;

import static com.flance.web.utils.AssertException.ErrCode.*;

/**
 * 内置接口限流器
 * 任意appId -> 调用某个接口 的频率限制
 * @author jhf
 */
@Slf4j
@Component
public class LimitApiFilter implements GatewayFilter, Ordered {

    @Resource
    private RouteApiService routeApiService;

    @Resource
    private RedisUtils redisUtils;

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 2;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String uri = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethodValue();
        String apiId = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_REQUEST_ID);
        String appId = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_APP_ID);
        String version =  exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_REQUEST_VERSION);

        // 校验接口编号
        if (StringUtils.isEmpty(apiId)) {
            apiId =  exchange.getRequest().getQueryParams().getFirst(RequestConstant.HEADER_REQUEST_ID);
        }
        if (StringUtils.isEmpty(apiId)) {
            log.error("请求接口编号为空，无法进行限流【appId:{}】【method:{}】【uri:{}】", appId, method, uri);
            return Mono.error(new NotFoundException("请求接口编号为空"));
        }
        if (StringUtils.isEmpty(appId)) {
            log.error("appId为空，无法进行api限流【method:{}】【uri:{}】【api_id:{}】", method, uri, apiId);
            return Mono.error(new NotFoundException("appId为空"));
        }

        RouteApiModel apiModel = routeApiService.getOneByApiIdAndVersion(apiId, version);
        if (null == apiModel) {
            log.error("找不到api【{}】，请确认是否启用【method:{}】【uri:{}】【api_id:{}】", appId, method, uri, apiId);
            return Mono.error(new NotFoundException("找不到api【" + apiId + "】"));
        }

        log.info("开始接口限流");
        Integer limit = apiModel.getApiLimit();
        if (null != limit && limit > 0) {
            String limitLock = appId + "_" + apiModel.getApiId();
            if (null == redisUtils.get(RequestConstant.LIMIT_API_KEY + ":" + limitLock)) {
                redisUtils.add(RequestConstant.LIMIT_API_KEY + ":" + limitLock, "1", limit.longValue());
            } else {
                AssertUtil.throwError(AssertException.getByEnum(SYS_GATEWAY_LIMIT_API_ERROR));
            }
        } else {
            log.info("接口[{}]未开启限流", apiModel.getApiId());
        }

        return chain.filter(exchange);
    }


}
