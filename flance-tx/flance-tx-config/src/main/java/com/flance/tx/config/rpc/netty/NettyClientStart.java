package com.flance.tx.config.rpc.netty;

import com.flance.tx.common.client.netty.CTNettyClient;
import com.flance.tx.common.client.netty.data.DataUtils;
import com.flance.tx.common.client.netty.data.NettyRequest;
import com.flance.tx.common.utils.GsonUtils;
import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import sun.misc.BASE64Encoder;

import javax.xml.crypto.Data;
import java.nio.charset.StandardCharsets;
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
                heartChannel.writeAndFlush(DataUtils.getStr(request).getBytes(StandardCharsets.UTF_8)).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
                heartChannel.close();
            }

        }


    }



}
