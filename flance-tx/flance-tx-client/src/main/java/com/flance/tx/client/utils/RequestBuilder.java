package com.flance.tx.client.utils;

import com.flance.tx.client.netty.ClientCallbackService;
import com.flance.tx.client.netty.configs.NettyClientConfig;
import com.flance.tx.common.netty.RoomContainer;
import com.flance.tx.common.utils.GsonUtils;
import com.flance.tx.netty.current.CurrentNettyData;
import com.flance.tx.netty.data.NettyRequest;
import io.netty.channel.Channel;

import java.util.UUID;

public class RequestBuilder {

    public static NettyRequest buildRequest(NettyClientConfig nettyClientConfig,
                                      ClientCallbackService clientCallbackService,
                                      Channel channel,
                                      String bizHandler,
                                      Object data) {
        String roomId = RoomContainer.getRoomId();
        if (null == roomId) {
            throw new RuntimeException("系统错误，找不到roomId，可能全局事务发起者切面未生效");
        }
        String messageId = UUID.randomUUID().toString();
        CurrentNettyData.putCallback2DataMap(messageId, channel, clientCallbackService);
        NettyRequest request = new NettyRequest();
        request.setHandlerId(bizHandler);
        request.setServerData(ClientUtil.getServerData());
        request.setRoomId(roomId);
        request.setMessageId(messageId);
        request.setIsHeartBeat(false);
        request.setData(GsonUtils.toJSONString(data));
        return request;
    }

}
