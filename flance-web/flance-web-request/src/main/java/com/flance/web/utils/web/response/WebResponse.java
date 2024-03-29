package com.flance.web.utils.web.response;

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

    public WebResponse() {

    }

    public WebResponse(Boolean success, String msg, String code, Object data, String sign, Long timestamp) {
        this.success = success;
        this.msg = msg;
        this.code = code;
        this.data = data;
        this.sign = sign;
        this.timestamp = timestamp;
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

    public <T> T getResultData(Class<T> clazz) {
        if (null == data) {
            return null;
        }
        try {
            Gson gson = new Gson();
            return gson.fromJson(gson.toJson(data), clazz);
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
            Gson gson = new Gson();
            List<T> list = Lists.newArrayList();
            JsonArray jsonArray = JsonParser.parseString(gson.toJson(data)).getAsJsonArray();
            jsonArray.forEach(ele -> list.add(gson.fromJson(ele, clazz)));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
