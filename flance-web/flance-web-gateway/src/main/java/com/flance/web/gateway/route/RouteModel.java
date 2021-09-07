package com.flance.web.gateway.route;

/**
 * 路由模型接口
 * @author jhf
 */
public interface RouteModel {

    /**
     * 获取路由id
     * @return  返回路由id
     */
    String getRouteId();

    /**
     * 获取路由名称
     * @return  返回路由名称
     */
    String getRouteName();

    /**
     * 获取路由路径（访问匹配路径）
     * @return  返回路由路径
     */
    String getRoutePath();

    /**
     * 获取服务的访问路径
     * @return  返回访问路径
     */
    String getRouteUri();

    /**
     * 获取路由编码
     * @return  返回路由编码
     */
    String getRouteCode();

    /**
     * 获取指定的过滤器
     * @return  返回过滤器
     */
    String getFilter();
}
