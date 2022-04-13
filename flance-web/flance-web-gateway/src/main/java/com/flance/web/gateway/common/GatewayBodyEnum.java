package com.flance.web.gateway.common;

public enum GatewayBodyEnum {

    RSA_ENCODE("rsa_encode"),
    RSA_DECODE("rsa_decode"),
    ;

    private final String code;

    GatewayBodyEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
