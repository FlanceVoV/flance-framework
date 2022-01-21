package com.flance.web.gateway.service;

import com.flance.web.utils.route.AppModel;

import java.util.List;

/**
 * 应用服务
 * @author jhf
 */
public interface AppService {

    /**
     * 获取所有对接应用
     * @return  应用集合
     */
    List<? extends AppModel> getApps();

    /**
     * 根据appId获取
     * @param appId  appId
     * @return       返回app模型
     */
    AppModel getAppByAppId(String appId);

}
