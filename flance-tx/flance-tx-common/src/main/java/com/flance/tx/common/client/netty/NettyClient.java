package com.flance.tx.common.client.netty;

import io.netty.channel.Channel;

public interface NettyClient {

    String getServerIp();

    Integer getServerPort();

    void start() throws Exception;

    Channel getChannel();

}
