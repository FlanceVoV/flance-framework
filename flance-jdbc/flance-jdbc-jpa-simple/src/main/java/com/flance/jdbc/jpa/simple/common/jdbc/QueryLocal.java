package com.flance.jdbc.jpa.simple.common.jdbc;

import com.alibaba.fastjson.JSONObject;

public class QueryLocal {
    public static ThreadLocal<Class> classLocal = new ThreadLocal();
    public static ThreadLocal<JSONObject> searchParamater = new ThreadLocal();
}
