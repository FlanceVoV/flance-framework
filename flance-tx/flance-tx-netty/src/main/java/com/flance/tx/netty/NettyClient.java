package com.flance.tx.netty;

import io.netty.channel.Channel;

public interface NettyClient {

    String getServerIp();

    Integer getServerPort();

    void start() throws Exception;

    Channel getChannel();

}
