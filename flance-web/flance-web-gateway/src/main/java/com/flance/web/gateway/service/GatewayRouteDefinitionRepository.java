package com.flance.web.gateway.service;

import com.flance.web.gateway.common.BizConstant;
import com.flance.web.utils.RedisUtils;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;


/**
 * 定义路由存储实现
 * @author jhf
 */
@Slf4j
@Service
public class GatewayRouteDefinitionRepository implements RouteDefinitionRepository {

    @Resource
    RedisUtils redisUtils;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(getRedisRouteDefinitions());
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return Mono.empty();
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return Mono.empty();
    }

    private List<RouteDefinition> getRedisRouteDefinitions() {
        Gson gson = new Gson();
        List<RouteDefinition> list = Lists.newArrayList();
        String data = redisUtils.get(BizConstant.ROUTER_KEY);
        if (!StringUtils.isEmpty(data)) {
            JsonArray jsonArray = JsonParser.parseString(data).getAsJsonArray();
            jsonArray.forEach(route -> list.add(gson.fromJson(route, RouteDefinition.class)));
        }
        return list;
    }

}
