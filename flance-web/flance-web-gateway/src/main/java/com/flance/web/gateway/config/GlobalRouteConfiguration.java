package com.flance.web.gateway.config;

import com.flance.web.gateway.service.RouteService;
import com.flance.web.utils.route.RouteModel;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.cloud.gateway.handler.predicate.RoutePredicateFactory.PATTERN_KEY;

/**
 * 全局动态路由配置
 * @author jhf
 */
@Slf4j
@Service
public class GlobalRouteConfiguration implements ApplicationEventPublisherAware {

    @Resource
    private RouteService routeService;

    private ApplicationEventPublisher publisher;

    private static final List<RouteDefinition> ROUTE_DEFINITIONS = Lists.newArrayList();

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public static List<RouteDefinition> getRouteDefinitions() {
        return ROUTE_DEFINITIONS;
    }


    @PostConstruct
    public void getRouteLocator() {

        if (null == routeService) {
            return;
        }

        // 获取所有动态路由
        List<? extends RouteModel> routes = routeService.getRouteLists();

        // 构建路由
//        RouteLocatorBuilder.Builder b = builder.routes();

        Optional.ofNullable(routes).orElse(Lists.newArrayList()).forEach(route -> {
            log.info("flance-gateway：加载路由 => [name: {}, id: {}, code: {}, path: {}, uri: {}, filter: {}]",
                    route.getRouteName(), route.getRouteId(), route.getRouteCode(), route.getRoutePath(), route.getRouteUri(), route.getFilter());
//            URI uri = URI.create(route.getRouteUri());
//            if (null == route.getFilter()) {
//                b.route(route.getRouteId(), dr -> dr.path(route.getRoutePath()).uri(uri));
//                return;
//            }
//            String[] filters = route.getFilter().split(",");
//            GatewayFilter[] gatewayFilters = new GatewayFilter[filters.length];
//            for (int i = 0; i < gatewayFilters.length; i++) {
//                gatewayFilters[i] = applicationContext.getBean(filters[i], GatewayFilter.class);
//            }
//            b.route(route.getRouteId(), dr -> dr.path(route.getRoutePath()).uri(uri).filters(gatewayFilters));
            ROUTE_DEFINITIONS.add(setDefaultDefinition(route));
        });

//        return b.build();
    }

    public void notifyChanged() {
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    private RouteDefinition setDefaultDefinition(RouteModel routeModel) {
        RouteDefinition definition = new RouteDefinition();
        Map<String, String> predicateParams = Maps.newHashMap();

        URI uri = URI.create(routeModel.getRouteUri());
        definition.setId(routeModel.getRouteId());
        definition.setUri(uri);

        PredicateDefinition predicate = new PredicateDefinition();
        predicateParams.put("pattern", routeModel.getRoutePath());
        predicate.setName("Path");
        predicate.setArgs(predicateParams);
        definition.setPredicates(Lists.newArrayList(predicate));

        if (null != routeModel.getFilter()) {
            String[] filters = routeModel.getFilter().split(",");
            List<FilterDefinition> filterDefinitions = Lists.newArrayList();
            for (int i = 0; i < filters.length; i++) {
                FilterDefinition filterDefinition = new FilterDefinition();
                filterDefinition.setName("Global");
                filterDefinition.addArg("filterName", filters[i]);
                filterDefinitions.add(filterDefinition);
            }
            definition.setFilters(filterDefinitions);
        }

        return definition;
    }


}
