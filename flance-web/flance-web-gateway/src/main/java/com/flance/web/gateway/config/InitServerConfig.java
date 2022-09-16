package com.flance.web.gateway.config;

import com.flance.web.gateway.common.BizConstant;
import com.flance.web.gateway.service.AppService;
import com.flance.web.gateway.service.RouteApiService;
import com.flance.web.gateway.service.RouteService;
import com.flance.web.utils.RedisUtils;
import com.flance.web.utils.route.AppModel;
import com.flance.web.utils.route.RouteApiModel;
import com.flance.web.utils.route.RouteModel;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class InitServerConfig {

    @Resource
    private RouteService routeService;

    @Resource
    private RouteApiService routeApiService;

    @Resource
    private AppService appService;

    @Resource
    private RedisUtils redisUtils;

    @Value("${flance.gateway.force-refresh:true}")
    private Boolean forceRefresh;

    /**
     * 清空app缓存
     */
    public void clearApp() {
        redisUtils.clear(BizConstant.APP_KEY + ":*");
    }

    /**
     * 清空路由
     */
    public void clearRouter() {
        redisUtils.clear(BizConstant.ROUTER_KEY);
        redisUtils.clear(BizConstant.ROUTER_MODEL_KEY + ":*");
    }

    /**
     * 清空路由
     */
    public void clearApi() {
        redisUtils.clear(BizConstant.API_KEY + ":*");
    }


    /**
     * 初始化路由
     */
    public void initRouter() {
        if (null == routeService) {
            log.error("网关启动异常-路由服务未注入");
            return;
        }

        if (redisUtils.keys(BizConstant.ROUTER_KEY + ":*").size() > 0 && !forceRefresh) {
            log.info("网关启动-加载路由模块-未开启强制刷新");
            return;
        }

        // 获取所有动态路由
        List<? extends RouteModel> routes = routeService.getRouteLists();
        Gson gson = new Gson();
        List<RouteDefinition> routeDefinitions = Lists.newArrayList();
        Optional.ofNullable(routes).orElse(Lists.newArrayList()).forEach(route -> {
            log.info("flance-gateway：加载路由 => [name: {}, id: {}, code: {}, path: {}, uri: {}, filter: {}]",
                    route.getRouteName(), route.getRouteId(), route.getRouteCode(), route.getRoutePath(), route.getRouteUri(), route.getFilter());
            routeDefinitions.add(setDefaultDefinition(route));
            redisUtils.add(BizConstant.ROUTER_MODEL_KEY + ":" + route.getRouteId(), gson.toJson(route));
        });

        redisUtils.add(BizConstant.ROUTER_KEY, gson.toJson(routeDefinitions));
    }

    /**
     * 初始化api
     */
    public void initApi() {
        if (null == routeApiService) {
            log.error("网关启动异常-接口服务未注入");
            return;
        }

        if (redisUtils.keys(BizConstant.API_KEY + ":*").size() > 0 && !forceRefresh) {
            log.info("网关启动-加载接口模块-未开启强制刷新");
            return;
        }
        List<? extends RouteApiModel> apis = routeApiService.getAllApi();
        Gson gson = new Gson();
        apis.forEach(item -> {
            String version = item.getApiVersion();
            if (!StringUtils.hasLength(item.getApiVersion())) {
                version = BizConstant.API_VERSION_DEFAULT;
            }
            if (null != redisUtils.get(BizConstant.API_KEY + ":" + item.getApiId() + ":" + version)) {
                return;
            }
            redisUtils.add(BizConstant.API_KEY + ":" + item.getApiId()  + ":" + version, gson.toJson(item));
        });
    }

    /**
     * 初始化app
     */
    public void initApp() {
        if (null == appService) {
            log.error("网关启动异常-应用服务未注入");
            return;
        }

        if (redisUtils.keys(BizConstant.APP_KEY + ":*").size() > 0 && !forceRefresh) {
            log.info("网关启动-加载应用模块-未开启强制刷新");
            return;
        }
        List<? extends AppModel> apps = appService.getApps();
        Gson gson = new Gson();
        apps.forEach(item -> {
            if (null != redisUtils.get(BizConstant.APP_KEY + ":" + item.getAppId())) {
                return;
            }
            redisUtils.add(BizConstant.APP_KEY + ":" + item.getAppId(), gson.toJson(item));
        });
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
