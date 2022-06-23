package com.flance.tx.common.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.*;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * gson工具类
 * @author jhf
 */
public class GsonUtils {

    /**
     * gson 对象转 json 不处理html 不返回空值字段
     */
    public static String toJSONString(Object obj) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return gson.toJson(obj);
    }

    /**
     * gson 对象转 json 不处理html 返回空值字段
     */
    public static String toJSONStringWithNull(Object obj) {
        Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
        return gson.toJson(obj);
    }

    public static <T> T fromString(String str, Class<T> clazz) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return gson.fromJson(str, clazz);
    }

    public static <T> T fromStringParse(String str, Class<T> clazz) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setFieldNamingStrategy(getLineToHump()).create();
        return gson.fromJson(str, clazz);
    }

    public static <T> Collection<T> fromStringArray(String str, Class<T> clazz) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        List<T> list = Lists.newArrayList();
        JsonArray jsonArray = JsonParser.parseString(str).getAsJsonArray();
        jsonArray.forEach(ele -> list.add(gson.fromJson(ele, clazz)));
        return list;
    }

    private static FieldNamingStrategy getLineToHump() {
        return f -> {
            String fieldName = f.getName();
            return FieldUtils.humpToLine(fieldName);
        };
    }

}
