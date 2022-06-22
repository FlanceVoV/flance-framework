package com.flance.tx.core.tx;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.flance.tx.core.annotation.FlanceGlobalTransactional;

public class TxThreadLocal {

    private static final TransmittableThreadLocal<FlanceGlobalTransactional.Module> TX_MODULE = new TransmittableThreadLocal<>();


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
