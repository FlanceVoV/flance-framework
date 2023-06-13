package com.flance.tx.server.netty.biz.wesocket;

import io.netty.channel.Channel;

public interface WebSocketUrlHandler {

    void doHandler(Channel channel, String uri);

}
