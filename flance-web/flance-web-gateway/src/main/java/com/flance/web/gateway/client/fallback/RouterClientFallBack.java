package com.flance.web.gateway.client.fallback;


import com.flance.web.gateway.client.RouterClient;
import com.flance.web.gateway.exception.GlobalGatewayException;

/**
 * 用户资源 fallback，可以继承
 * @author jhf
 */
public class RouterClientFallBack {

    public RouterClient create(Throwable throwable) {
        return () -> {
            throwable.printStackTrace();
            throw new GlobalGatewayException("路由资源加载失败[getRouters]！");
        };
    }

}
