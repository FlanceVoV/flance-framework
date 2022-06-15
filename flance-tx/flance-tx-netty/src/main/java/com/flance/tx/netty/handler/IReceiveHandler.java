package com.flance.tx.netty.handler;


public interface IReceiveHandler<T, R> {

    T handler(String msg);

    T handler(R msg);

}
