package com.flance.tx.common.client.netty.biz;

public interface IBizHandler<T, R> {

    T doBizHandler(R r);

}
