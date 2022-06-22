package com.flance.tx.common.utils;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class TxUtils {

    private static final TransmittableThreadLocal<String> TX_ID = new TransmittableThreadLocal<>();

    public static String getTxId() {
        return TX_ID.get();
    }

    public static String getTxId(String headerLogId) {
        String logId = getTxId();
        if (null == logId) {
            logId = headerLogId;
        }
        if (null == logId) {
            logId = UUID.randomUUID().toString();
            setTxId(logId);
        }
        return logId;
    }

    public static void remove() {
        log.debug("释放thread_local[{}]", TX_ID.get());
        if (null != TX_ID.get()) {
            TX_ID.remove();
        }
    }

    public static void setTxId(String requestId) {
        TX_ID.set(requestId);
    }

}
