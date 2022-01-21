package com.flance.web.gateway.filter;


import com.flance.web.gateway.service.RouteApiService;
import com.flance.web.utils.RequestConstant;
import com.flance.web.utils.route.RouteApiModel;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

/**
 * 统一入口 请求分发
 * @author jhf
 */
@Slf4j
@Component
public class GatewayApiDispatcherFilter implements GatewayFilter {

    @Resource
    RouteLocator routeLocator;

    @Resource
    RouteApiService routeApiService;

    @Resource
    ApplicationContext applicationContext;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String uri = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethodValue();
        String appId = exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_APP_ID);
        String apiId =  exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_REQUEST_ID);
        String version =  exchange.getRequest().getHeaders().getFirst(RequestConstant.HEADER_REQUEST_VERSION);
        if (StringUtils.isEmpty(apiId)) {
            apiId =  exchange.getRequest().getQueryParams().getFirst(RequestConstant.HEADER_REQUEST_ID);
        }
        if (StringUtils.isEmpty(appId)) {
            appId =  exchange.getRequest().getQueryParams().getFirst(RequestConstant.HEADER_APP_ID);
        }
        if (StringUtils.isEmpty(apiId)) {
            log.error("请求接口编号为空，无法进行转发【appId:{}】【method:{}】【uri:{}】", appId, method, uri);
            return Mono.error(new NotFoundException("请求接口编号为空"));
        }
        log.info("【appId:{}】请求接口【({}){}】", appId, method, uri);
        RouteApiModel routeApiModel = routeApiService.getOneByApiIdAndVersion(apiId, version);
        if (null == routeApiModel) {
            log.error("接口不存在，无法进行转发【appId:{}】【method:{}】【uri:{}】", appId, method, uri);
            return Mono.error(new NotFoundException("接口不存在"));
        }
        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
        if (null != route) {
            String url = routeApiModel.getApiUri() + routeApiModel.getApiPath();
            log.info("【appId:{}】切换路由【({}){}】", appId, method, url);
            ServerHttpRequest request = changeRoute(exchange, routeApiModel);
            return chain.filter(exchange.mutate().request(request).build());
        }
        return chain.filter(exchange);
    }

    private ServerHttpRequest changeRoute(ServerWebExchange exchange, RouteApiModel routeApiModel) {
        String url = routeApiModel.getApiUri() + routeApiModel.getApiPath();
        URI uri = UriComponentsBuilder.fromUriString(url).build().toUri();
        ServerHttpRequest request = exchange.getRequest().mutate().uri(uri).build();
        Route target = getRoute(routeApiModel.getRouteId());
        Route newRoute = Route.async()
                .asyncPredicate(target.getPredicate())
                .filters(getFilters(routeApiModel))
                .id(target.getId())
                .order(target.getOrder())
                .uri(uri)
                .build();
        exchange.getAttributes().put(GATEWAY_ROUTE_ATTR, newRoute);
        return request;
    }

    private List<GatewayFilter> getFilters(RouteApiModel routeApiModel) {
        List<GatewayFilter> filters = Lists.newArrayList();
        String routeFilters = routeApiModel.getRouteModel().getFilter();
        String apiFilters = routeApiModel.getApiFilter();
        if (null != routeFilters) {
            for (String filterName : routeFilters.split(",")) {
                filters.add(getFilter(filterName));
            }
        }
        if (null != apiFilters) {
            for (String filterName : apiFilters.split(",")) {
                filters.add(getFilter(filterName));
            }
        }
        return filters;
    }

    private GatewayFilter getFilter(String beanName) {
        return applicationContext.getBean(beanName, GatewayFilter.class);
    }

    private Route getRoute(String routeId) {
        Mono<Map<String, Route>> routes = routeLocator.getRoutes().collectMap(Route::getId);
        AtomicReference<Route> routeReference = new AtomicReference<>();
        routes.subscribe(routeMap -> {
            Route route = routeMap.get(routeId);
            routeReference.set(route);
        });
        return routeReference.get();
    }
}
