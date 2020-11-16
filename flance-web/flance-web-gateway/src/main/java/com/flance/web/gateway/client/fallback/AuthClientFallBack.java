package com.flance.web.gateway.client.fallback;


import com.flance.web.gateway.client.AuthClient;

/**
 * authClient fallback，可以继承
 * @author jhf
 */
public class AuthClientFallBack {

    public AuthClient create(Throwable throwable) {
        return (permissionRequest) -> {
            throwable.printStackTrace();
            throw new RuntimeException("鉴权服务调用失败[hasPermission]！");
        };
    }

}
