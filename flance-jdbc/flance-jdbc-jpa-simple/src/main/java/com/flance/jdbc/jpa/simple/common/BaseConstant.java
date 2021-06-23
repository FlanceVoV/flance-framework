package com.flance.jdbc.jpa.simple.common;

/**
 * 业务常量
 * @author jhf
 */
public interface BaseConstant {

    /**
     * 删除标记-删除
     */
    Short DELETE_FLAG_DELETE = 1;

    /**
     * 删除标记-正常
     */
    Short DELETE_FLAG_NORMAL = 0;

    /**
     * 错误响应码-多设备登录
     */
    String ERR_CODE_MULTI_LOGIN = "10010";

    /**
     * 错误响应码-session失效
     */
    String ERR_CODE_NULL_SESSION = "10020";

    /**
     * 图形验证码session存储key
     */
    String SESSION_CAPTCHA_KEY = "lineCaptcha";

    /**
     * 短信验证码session存储key
     */
    String SESSION_SMS_KEY = "sms";

}
