package com.flance.web.utils;

public class RequestUtil {

    private static final ThreadLocal<String> REQUEST_ID = new ThreadLocal<>();

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
