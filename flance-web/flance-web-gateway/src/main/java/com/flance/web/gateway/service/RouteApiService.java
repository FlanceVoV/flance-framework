package com.flance.web.gateway.service;

import com.flance.web.utils.route.RouteApiModel;

import java.util.List;

public interface RouteApiService {

    List<? extends RouteApiModel> getAllApi();

    /**
     * 根据apiId和版本号查询接口，如果版本号为空则返回最新的
     * @param apiId     api标识
     * @param version   版本号
     * @return          返回api
     */
    RouteApiModel getOneByApiIdAndVersion(String apiId, String version);

}
