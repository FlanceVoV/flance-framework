package com.flance.tx.netty.handler;


import io.netty.channel.Channel;

public interface IReceiveHandler<T, R> {

    T handler(String msg, Channel channel);

    T handler(R msg, Channel channel);

}
