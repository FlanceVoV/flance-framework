package com.flance.web.utils.route;

/**
 * 资源接口
 * @author jhf
 */
public interface RouteApiModel {

    /**
     * 资源接口唯一标识
     * @return S
     */
    String getApiId();

    /**
     * 资源接口所属路由服务唯一标识，非主键
     * @return  S
     */
    String getRouteId();

    /**
     * 资源接口名称
     * @return  S
     */
    String getApiName();


    /**
     * 资源接口uri
     * @return  S
     */
    String getApiUri();

    /**
     * 资源接口 path
     * @return  S
     */
    String getApiPath();

    /**
     * 资源接口版本号
     * @return  S
     */
    String getApiVersion();


    /**
     * 获取指定的过滤器
     * @return  返回过滤器
     */
    String getApiFilter();

    /**
     * 获取全局限流配置 单位： 请求次数/毫秒
     * @return  返回限流
     */
    Integer getApiLimit();

    /**
     * 获取接口超时配置 单位：ms
     * @return  返回超时
     */
    Integer getApiTimeOut();

    /**
     * 获取服务路由
     * @return  返回服务路由
     */
    RouteModel getRouteModel();

}
