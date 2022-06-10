package com.flance.tx.config.rpc.netty;

import com.flance.tx.common.client.netty.CTNettyClient;
import com.flance.tx.common.client.netty.data.NettyRequest;
import com.flance.tx.common.utils.GsonUtils;
import io.netty.channel.Channel;

import java.util.UUID;

public class NettyClientStart {

    public static void startNettyClient() {
        try {
            CTNettyClient ctNettyClient = new CTNettyClient("127.0.0.1", 8899);
            ctNettyClient.start();
            new Thread(NettyClientStart::sendHeartBeat).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendHeartBeat() {

        Channel heartChannel;

        try {
            CTNettyClient ctNettyClient = new CTNettyClient("127.0.0.1", 8899);
            ctNettyClient.start();
            heartChannel = ctNettyClient.getChannel();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        NettyRequest request = new NettyRequest();
        request.setIsHeartBeat(true);
        request.setData("心跳检查");
        while (true) {
            request.setMessageId(UUID.randomUUID().toString());
            try {
                heartChannel.writeAndFlush(GsonUtils.toJSONString(request) + "\r\n");
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
                heartChannel.close();
            }

        }
    }

}
