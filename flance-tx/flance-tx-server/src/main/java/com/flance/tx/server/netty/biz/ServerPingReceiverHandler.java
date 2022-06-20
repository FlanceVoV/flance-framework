package com.flance.tx.server.netty.biz;

import com.flance.tx.netty.biz.IBizHandler;
import com.flance.tx.netty.container.RoomContainer;
import com.flance.tx.netty.container.rooms.ConnectionRoom;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import com.flance.tx.netty.data.ServerData;
import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component("serverPingReceiverHandler")
public class ServerPingReceiverHandler implements IBizHandler<NettyResponse, NettyRequest> {

    @Override
    public NettyResponse doBizHandler(NettyRequest request, Channel channel) {
        NettyResponse response = new NettyResponse();
        ServerData serverData = new ServerData();

        if(null == RoomContainer.getRoom(request.getRoomId())) {
            String roomId = request.getRoomId();
            String roomName = "ping room -" + roomId;
            Map<String, Channel> channelMap = Maps.newConcurrentMap();
            channelMap.put(request.getServerData().getId(), channel);
            ConnectionRoom connectionRoom = new ConnectionRoom(roomId, roomName, channelMap);
            RoomContainer.createRoom(roomId, connectionRoom);
        } else {
            ConnectionRoom connectionRoom = (ConnectionRoom) RoomContainer.getRoom(request.getRoomId());
            if (null == connectionRoom.getChannelById(request.getServerData().getId())) {
                connectionRoom.addChannel(request.getServerData().getId(), channel);
            }
        }

        log.info("get ping success");
        response.setIsHeartBeat(request.getIsHeartBeat());
        response.setMessageId(request.getMessageId());
        response.setRequest(request);
        response.setHandlerId("clientPongReceiverHandler");
        response.setServerData(serverData);
        return response;
    }

}
