package com.flance.web.utils;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Slf4j
public class RequestUtil {

    private static final TransmittableThreadLocal<String> REQUEST_ID = new TransmittableThreadLocal<>();

    public static String getLogId() {
        return REQUEST_ID.get();
    }

    public static String getLogId(String headerLogId) {
        String logId = getLogId();
        if (null == logId) {
            logId = headerLogId;
        }
        if (null == logId) {
            logId = UUID.randomUUID().toString();
            setLogId(logId);
        }
        return logId;
    }

    public static void remove() {
        log.debug("释放thread_local[{}]", REQUEST_ID.get());
        if (null != REQUEST_ID.get()) {
            REQUEST_ID.remove();
        }
    }

    public static void setLogId(String requestId) {
        REQUEST_ID.set(requestId);
    }


}
