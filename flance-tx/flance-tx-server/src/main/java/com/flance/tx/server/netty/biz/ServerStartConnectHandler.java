package com.flance.tx.server.netty.biz;

import com.flance.tx.common.netty.Room;
import com.flance.tx.common.netty.RoomContainer;
import com.flance.tx.netty.biz.IBizHandler;
import com.flance.tx.netty.container.rooms.ConnectionRoom;
import com.flance.tx.netty.data.DataUtils;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import com.flance.tx.server.netty.utils.ServerUtil;
import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component("serverStartConnectHandler")
public class ServerStartConnectHandler implements IBizHandler<NettyResponse, NettyRequest> {

    @Override
    public NettyResponse doBizHandler(NettyRequest request, Channel channel) {
        NettyResponse response = new NettyResponse();
        response.setIsHeartBeat(request.getIsHeartBeat());
        response.setMessageId(request.getMessageId());
        response.setRequest(request);
        response.setHandlerId("clientPongReceiverHandler");
        response.setServerData(ServerUtil.getServerData());
        response.setRoomId(request.getRoomId());

        Map<String, Channel> channelMap = Maps.newConcurrentMap();
        String channelId = request.getRoomId();
        if (null != request.getServerData() && null != request.getServerData().getId()) {
            channelId = request.getServerData().getId();
        }
        channelMap.put(channelId, channel);
        ConnectionRoom connectionRoom = new ConnectionRoom(request.getRoomId(), request.getRoomId(), channelMap);
        RoomContainer.createRoom(request.getRoomId(), connectionRoom);

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Channel clientChannel = connectionRoom.getChannelById(request.getRoomId());
                if (clientChannel.isActive() && clientChannel.isOpen()) {
                    clientChannel.writeAndFlush(DataUtils.getStr(response).getBytes(StandardCharsets.UTF_8)).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                } else {
                    clientChannel.close();
                    return;
                }
            }
        }).start();

        return response;
    }
}
