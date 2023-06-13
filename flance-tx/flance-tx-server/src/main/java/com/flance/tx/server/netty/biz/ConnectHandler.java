package com.flance.tx.server.netty.biz;

import com.flance.tx.common.netty.Room;
import com.flance.tx.common.netty.RoomContainer;
import com.flance.tx.netty.biz.IBizHandler;
import com.flance.tx.netty.container.rooms.ConnectionRoom;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * roomId - 客户端编号
 */
@Component("connectHandler")
public class ConnectHandler implements IBizHandler<NettyResponse, NettyRequest> {

    @Override
    public NettyResponse doBizHandler(NettyRequest request, Channel channel) {
        Map<String, Channel> channelMap = Maps.newConcurrentMap();
        channelMap.put(request.getClientId(), channel);
        Room room = RoomContainer.getRoom(request.getRoomId());
        if (null == room) {
            room = new ConnectionRoom(request.getRoomId(), request.getRoomId(), channelMap);
            RoomContainer.createRoom(request.getRoomId(), room);
        } else {
            room.getChannels().putAll(channelMap);
        }
        NettyResponse response = new NettyResponse();
        response.setRoomId(request.getRoomId());
        response.setSuccess(true);
        response.setMessageId(request.getMessageId());
        response.setClientId(request.getClientId());
        return response;
    }
}
