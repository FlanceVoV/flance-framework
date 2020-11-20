package com.flance.web.gateway.exception;

/**
 * 全局网关异常
 * @author jhf
 */
public class GlobalGatewayException extends RuntimeException {


    public GlobalGatewayException() {
        super();
    }

    public GlobalGatewayException(String msg) {
        super(msg);
    }
}
