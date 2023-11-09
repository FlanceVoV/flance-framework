package com.flance.web.utils.web.response;

import com.flance.web.utils.GsonUtils;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WebResponse {

    private Boolean success;

    private String msg;

    private String code;

    /**
     * 如果加密
     * 则：原数据转 to jsonStr 加密 -> base64字符串
     */
    private Object data;

    /**
     * 签名 = sign(data转base64 + timestamp)
     */
    private String sign;

    private Long timestamp;

    /**
     * 相响应是否加密
     */
    private Integer isEncode;

    /**
     * 加密方式
     */
    private String encodeType;

    /**
     * 签名方式
     */
    private String signType;

    /**
     * debug信息
     */
    private String debug;

    public WebResponse() {

    }

    public WebResponse(Boolean success, String msg, String code, Object data, String sign, Long timestamp, Integer isEncode, String encodeType, String signType, String debug) {
        this.success = success;
        this.msg = msg;
        this.code = code;
        this.data = data;
        this.sign = sign;
        this.timestamp = timestamp;
        this.isEncode = isEncode;
        this.encodeType = encodeType;
        this.signType = signType;
        this.debug = debug;
    }

    public static WebResponse getSucceed(Object data, String msg) {
        return WebResponse.builder().code("000000").success(true).msg(msg).data(data).build();
    }

    public static WebResponse getFailed(String code, String msg) {
        return WebResponse.builder().code(code).success(false).msg(msg).build();
    }

    public static WebResponse getFailed(Object data, String code, String msg) {
        return WebResponse.builder().data(data).code(code).success(false).msg(msg).build();
    }

    public static WebResponse getFailedDebug(String code, String msg, String debug) {
        return WebResponse.builder().code(code).success(false).msg(msg).debug(debug).build();
    }

    public static WebResponse getFailedDebug(Object data, String code, String msg, String debug) {
        return WebResponse.builder().data(data).code(code).success(false).msg(msg).debug(debug).build();
    }

    public <T> T getResultData(Class<T> clazz) {
        if (null == data) {
            return null;
        }
        try {
            return GsonUtils.fromString(data.toString(), clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> List<T> getResultList(Class<T> clazz) {
        if (null == data) {
            return null;
        }
        try {
            return (List<T> ) GsonUtils.fromStringArray(data.toString(), clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
