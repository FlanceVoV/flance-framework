package com.flance.web.gateway.client.fallback;


import com.flance.web.gateway.client.RouterApiClient;
import com.flance.web.gateway.client.RouterClient;
import com.flance.web.gateway.exception.GlobalGatewayException;

/**
 * 用户资源 fallback，可以继承
 * @author jhf
 */
public class RouterApiClientFallBack {

    public RouterApiClient create(Throwable throwable) {
        return () -> {
            throwable.printStackTrace();
            throw new GlobalGatewayException("api资源加载失败[getApis]！");
        };
    }

}
