package com.flance.web.gateway.service;



import com.flance.web.utils.route.RouteModel;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.List;

/**
 * 动态路由接口
 * @author jhf
 */
public interface RouteService {

    /**
     * 获取所有动态路由
     * @return  返回路由列表
     */
    List<? extends RouteModel> getRouteLists();

    /**
     * 根据路由id获取路由实例
     * @param routeId   路由id
     * @return          路由
     */
    RouteDefinition getRouteById(String routeId);

}
