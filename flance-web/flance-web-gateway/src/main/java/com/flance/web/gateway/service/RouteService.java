package com.flance.web.gateway.service;

import com.flance.web.gateway.route.RouteModel;

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
    List<RouteModel> getRouteLists();

}
