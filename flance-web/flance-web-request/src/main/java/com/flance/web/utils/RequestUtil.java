package com.flance.web.utils;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestUtil {

    private static final TransmittableThreadLocal<String> REQUEST_ID = new TransmittableThreadLocal<>();

    public static String getLogId() {
        return REQUEST_ID.get();
    }

    public static void remove() {
        log.info("释放资源");
        REQUEST_ID.remove();
    }

    public static void setLogId(String requestId) {
        REQUEST_ID.set(requestId);
    }


}
