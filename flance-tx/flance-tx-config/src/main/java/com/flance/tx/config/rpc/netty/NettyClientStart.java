package com.flance.tx.config.rpc.netty;

import com.flance.tx.common.client.netty.CTNettyClient;
import io.netty.channel.Channel;

public class NettyClientStart {

    public static void startNettyClient() {
        try {
            CTNettyClient ctNettyClient = new CTNettyClient("127.0.0.1", 8899);
            ctNettyClient.start();
            Channel channel = ctNettyClient.getChannel();
            channel.writeAndFlush("client - ping \r\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
