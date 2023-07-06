package com.flance.tx.netty.handler;

import io.netty.channel.Channel;

public interface ITcpReceiveHandler {

    String handler(String msg, Channel channel);

}
