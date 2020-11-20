package com.flance.web.gateway.client.fallback;


import com.flance.web.gateway.client.UserResourceClient;
import com.flance.web.gateway.exception.GlobalGatewayException;

/**
 * 用户资源 fallback，可以继承
 * @author jhf
 */
public class UserResourceClientFallBack {

    public UserResourceClient create(Throwable throwable) {
        return (permissionRequest) -> {
            throwable.printStackTrace();
            throw new GlobalGatewayException("用户资源调用失败[getUserInfo]！");
        };
    }

}
