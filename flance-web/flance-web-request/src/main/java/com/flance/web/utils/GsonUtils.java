package com.flance.web.utils;

import com.google.common.collect.Lists;
import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(LocalDateTime.class, new LocalDataTypeAdapter())
                .registerTypeAdapter(new TypeToken<Map>(){}.getType(), new MapTypeAdapter())
                .disableHtmlEscaping().create();
        return gson.toJson(obj);
    }

    /**
     * gson 对象转 json 不处理html 返回空值字段
     */
    public static String toJSONStringWithNull(Object obj) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(LocalDateTime.class, new LocalDataTypeAdapter())
                .serializeNulls().disableHtmlEscaping().create();
        return gson.toJson(obj);
    }

    public static <T> T fromString(String str, Class<T> clazz) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(LocalDateTime.class, new LocalDataTypeAdapter())
                .registerTypeAdapter(new TypeToken<Map>(){}.getType(), new MapTypeAdapter())
                .disableHtmlEscaping().create();
        return gson.fromJson(str, clazz);
    }


    public static <T> T fromStringParse(String str, Class<T> clazz) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(LocalDateTime.class, new LocalDataTypeAdapter())
                .registerTypeAdapter(new TypeToken<Map>(){}.getType(), new MapTypeAdapter())
                .disableHtmlEscaping().setFieldNamingStrategy(getLineToHump()).create();
        return gson.fromJson(str, clazz);
    }

    public static <T> Collection<T> fromStringArray(String str, Class<T> clazz) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(LocalDateTime.class, new LocalDataTypeAdapter())
                .registerTypeAdapter(new TypeToken<Map>(){}.getType(), new MapTypeAdapter())
                .disableHtmlEscaping().create();
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


    public static class MapTypeAdapter extends TypeAdapter<Object> {

        @Override
        public Object read(JsonReader in) throws IOException {
            JsonToken token = in.peek();
            switch (token) {
                case BEGIN_ARRAY:
                    List<Object> list = new ArrayList<Object>();
                    in.beginArray();
                    while (in.hasNext()) {
                        list.add(read(in));
                    }
                    in.endArray();
                    return list;

                case BEGIN_OBJECT:
                    Map<String, Object> map = new LinkedTreeMap<String, Object>();
                    in.beginObject();
                    while (in.hasNext()) {
                        map.put(in.nextName(), read(in));
                    }
                    in.endObject();
                    return map;

                case STRING:
                    return in.nextString();

                case NUMBER:
                    /**
                     * 改写数字的处理逻辑，将数字值分为整型与浮点型。
                     */
                    double dbNum = in.nextDouble();

                    // 数字超过long的最大值，返回浮点类型
                    if (dbNum > Long.MAX_VALUE) {
                        return dbNum;
                    }

                    // 判断数字是否为整数值
                    long lngNum = (long) dbNum;
                    if (dbNum == lngNum) {
                        return lngNum;
                    } else {
                        return dbNum;
                    }

                case BOOLEAN:
                    return in.nextBoolean();

                case NULL:
                    in.nextNull();
                    return null;

                default:
                    throw new IllegalStateException();
            }
        }


        @Override
        public void write(JsonWriter jsonWriter, Object o) throws IOException {

        }


    }
}
