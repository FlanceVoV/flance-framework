package com.flance.web.utils;

import com.alibaba.ttl.TransmittableThreadLocal;

public class WebLogUtil {

    private static TransmittableThreadLocal<String> LOG_ID = new TransmittableThreadLocal<>();

    public static void setLogId(String s){
        LOG_ID.set(s);
    }

    public static String getLogId(){
        return LOG_ID.get();
    }

}
