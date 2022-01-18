package com.flance.web.utils;

import com.alibaba.ttl.TransmittableThreadLocal;

public class RequestUtil {

    private static final TransmittableThreadLocal<String> REQUEST_ID = new TransmittableThreadLocal<>();

    public static String getLogId() {
        return REQUEST_ID.get();
    }

    public static void remove() {
        REQUEST_ID.remove();
    }

    public static void setRequestId(String requestId) {
        REQUEST_ID.set(requestId);
    }

}
