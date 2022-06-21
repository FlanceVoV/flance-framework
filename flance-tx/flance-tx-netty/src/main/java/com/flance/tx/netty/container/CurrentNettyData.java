package com.flance.tx.netty.container;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.flance.tx.netty.data.NettyResponse;

public class CurrentNettyData {

    private static final TransmittableThreadLocal<Object> NETTY_RESPONSE = new TransmittableThreadLocal<>();

    public static <T> void addResponse(T t) {
        NETTY_RESPONSE.set(t);
    }

    public static <T> T getResponse() {
        return (T) NETTY_RESPONSE.get();
    }

    public static void remove() {
        NETTY_RESPONSE.remove();
    }

}
