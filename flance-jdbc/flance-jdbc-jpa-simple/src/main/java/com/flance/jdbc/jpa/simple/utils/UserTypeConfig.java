package com.flance.jdbc.jpa.simple.utils;

import com.flance.web.security.common.user.SecurityUserInfoService;
import com.google.common.collect.Maps;

import java.util.Map;

public class UserTypeConfig {

    private static final Map<String, Class<? extends SecurityUserInfoService>> userTypeMap = Maps.newHashMap();

    public static Class<? extends SecurityUserInfoService> getUserService(String key) {
        return userTypeMap.get(key);
    }

    public static void putUserService(String key, Class<? extends SecurityUserInfoService> service) {
        userTypeMap.put(key, service);
    }

}
