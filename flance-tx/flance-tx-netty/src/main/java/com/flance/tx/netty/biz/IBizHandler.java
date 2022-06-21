package com.flance.tx.netty.biz;

import io.netty.channel.Channel;

public interface IBizHandler<T, R> {

    T doBizHandler(R r, Channel channel);

}
