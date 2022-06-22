package com.flance.tx.common.utils;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.util.Collection;
import java.util.List;

/**
 * gson工具类
 * @author jhf
 */
public class GsonUtils {

    public static String toJSONString(Object obj) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return gson.toJson(obj);
    }

    public static String toJSONStringWithNull(Object obj) {
        Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
        return gson.toJson(obj);
    }

    public static <T> T fromString(String str, Class<T> clazz) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return gson.fromJson(str, clazz);
    }

    public static <T> Collection<T> fromStringArray(String str, Class<T> clazz) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        List<T> list = Lists.newArrayList();
        JsonArray jsonArray = JsonParser.parseString(str).getAsJsonArray();
        jsonArray.forEach(ele -> list.add(gson.fromJson(ele, clazz)));
        return list;
    }
}
