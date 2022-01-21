package com.flance.web.gateway.client.fallback;


import com.flance.web.gateway.client.AppClient;
import com.flance.web.gateway.client.AuthClient;
import com.flance.web.gateway.exception.GlobalGatewayException;

/**
 * appClient fallback，可以继承
 * @author jhf
 */
public class AppClientFallBack {

    public AppClient create(Throwable throwable) {
        return () -> {
            throwable.printStackTrace();
            throw new GlobalGatewayException("应用服务调用失败[appClient]！");
        };
    }

}
