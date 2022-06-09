package com.flance.tx.core.tx;

import com.flance.tx.core.annotation.FlanceGlobalTransactional;
import com.google.common.collect.Maps;

import java.util.Map;

public class TxThreadLocal {

    private static final ThreadLocal<FlanceGlobalTransactional.Module> TX_MODULE = new ThreadLocal<>();

    private static final Map<String, Object> TX_CONNECTION = Maps.newConcurrentMap();

    public static FlanceGlobalTransactional.Module getTxModule() {
        return TX_MODULE.get();
    }

    public static void setTxModule(FlanceGlobalTransactional.Module txModule) {
        TX_MODULE.set(txModule);
    }

    public static void removeTxModule() {
        TX_MODULE.remove();
    }
}
