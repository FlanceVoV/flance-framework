package com.flance.web.utils.route;

public interface AppApiLimitModel {

    /**
     * 租户服务 appId
     */
    String getAppId();

    /**
     * 接口
     */
    String getApiId();

    /**
     * 最大次数
     */
    Integer getLimitCount();

    /**
     * 时间 单位 s
     */
    Integer getLimitSecond();

}
