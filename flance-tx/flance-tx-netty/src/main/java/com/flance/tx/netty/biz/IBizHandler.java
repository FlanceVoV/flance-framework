package com.flance.tx.netty.biz;

public interface IBizHandler<T, R> {

    T doBizHandler(R r);

}
