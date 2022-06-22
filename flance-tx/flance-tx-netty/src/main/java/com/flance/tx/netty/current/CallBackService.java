package com.flance.tx.netty.current;

import java.util.concurrent.TimeUnit;

public interface CallBackService<T> {

    void awaitThread(int timeout, TimeUnit unit) throws InterruptedException;

    void receiveMessage(T data) throws Exception;

    T getData();

}
