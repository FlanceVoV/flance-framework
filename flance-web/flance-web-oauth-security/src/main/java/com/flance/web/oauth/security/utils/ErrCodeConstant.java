package com.flance.web.oauth.security.utils;

/**
 * 异常code常量
 * @author jhf
 */
public interface ErrCodeConstant {

    /**
     * token为空
     */
    String ERROR_TOKEN_NULL = "010001";

    /**
     * Token was not recognised
     * access_token校验失败
     */
    String ERROR_TOKEN_RECOGNISED = "010002";

    /**
     * Token has expired
     * access_token已经过期
     */
    String ERROR_TOKEN_EXPIRED = "010003";

    /**
     * token校验失败，未知异常
     */
    String ERROR_TOKEN_UNKNOWN = "010000";

    /**
     * auth鉴定失败，未知异常
     */
    String ERROR_AUTH_UNKNOWN = "020000";

    /**
     * 没有权限
     */
    String ERROR_AUTH_ACCESS = "020001";


}
