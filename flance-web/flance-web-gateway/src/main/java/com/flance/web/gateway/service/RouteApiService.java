package com.flance.web.gateway.service;

import com.flance.web.utils.route.AppApiLimitModel;
import com.flance.web.utils.route.RouteApiModel;

import java.util.List;

public interface RouteApiService {

    List<? extends RouteApiModel> getAllApi(boolean async);

    /**
     * 根据apiId和版本号查询接口，如果版本号为空则返回最新的
     * @param apiId     api标识
     * @param version   版本号
     * @return          返回api
     */
    RouteApiModel getOneByApiIdAndVersion(String apiId, String version);

    /**
     * 根据appId和apiId找到限流配置
     * @param appId     app标识
     * @param apiId     接口编号
     * @return          返回限流
     */
    AppApiLimitModel getOneByAppIdAndApiId(String appId, String apiId);

}
