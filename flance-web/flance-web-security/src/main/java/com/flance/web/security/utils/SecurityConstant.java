package com.flance.web.security.utils;

/**
 * security 异常常量
 * @author jhf
 */
public interface SecurityConstant {

    /**
     * 没有权限，权限信息为空
     */
    String ERROR_NULL_AUTH = "010001";

    /**
     * 找不到用户
     */
    String ERROR_NULL_USER = "010002";

    /**
     * 多端登录
     */
    String ERROR_MULTI_LOGIN = "010003";

    /**
     * 会话失效
     */
    String ERROR_NULL_SESSION = "010004";

    /**
     * 密码错误
     */
    String ERROR_PASSWORD = "010005";

    /**
     * 未知异常
     */
    String ERROR_UNKNOWN = "010000";

}
