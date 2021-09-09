package com.flance.web.gateway.config;

import com.flance.web.gateway.route.RouteModel;
import com.flance.web.gateway.service.RouteService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * 全局动态路由配置
 * @author jhf
 */
@Slf4j
@Configuration
public class GlobalRouteConfiguration {

    @Resource
    RouteService routeService;

    @Resource
    ApplicationContext applicationContext;

    @Bean
    public RouteLocator getRouteLocator(RouteLocatorBuilder builder) {

        if (null == routeService) {
            return null;
        }

        // 获取所有动态路由
        List<RouteModel> routes = routeService.getRouteLists();

        // 构建路由
        RouteLocatorBuilder.Builder b = builder.routes();

        Optional.ofNullable(routes).orElse(Lists.newArrayList()).forEach(route -> {
            log.info("flance-gateway：加载路由 => [name: {}, id: {}, code: {}, path: {}, uri: {}, filter: {}]",
                    route.getRouteName(), route.getRouteId(), route.getRouteCode(), route.getRoutePath(), route.getRouteUri(), route.getFilter());
            URI uri = URI.create(route.getRouteUri());
            if (null == route.getFilter()) {
                b.route(route.getRouteId(), dr -> dr.path(route.getRoutePath()).uri(uri));
                return;
            }
            String[] filters = route.getFilter().split(",");
            GatewayFilter[] gatewayFilters = new GatewayFilter[filters.length];
            for (int i = 0; i < gatewayFilters.length; i++) {
                gatewayFilters[i] = applicationContext.getBean(filters[i], GatewayFilter.class);
            }
            b.route(route.getRouteId(), dr -> dr.path(route.getRoutePath()).uri(uri).filters(gatewayFilters));
        });

        return b.build();
    }

}
