package com.flance.web.gateway.client.fallback;


import com.flance.web.gateway.client.AuthClient;
import com.flance.web.gateway.exception.GlobalGatewayException;

/**
 * authClient fallback，可以继承
 * @author jhf
 */
public class AuthClientFallBack {

    public AuthClient create(Throwable throwable) {
        return (permissionRequest) -> {
            throwable.printStackTrace();
            throw new GlobalGatewayException("鉴权服务调用失败[hasPermission]！");
        };
    }

}
