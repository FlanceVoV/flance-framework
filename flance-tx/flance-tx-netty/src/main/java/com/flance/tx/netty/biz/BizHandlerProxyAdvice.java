package com.flance.tx.netty.biz;

import com.flance.tx.common.netty.RoomContainer;
import com.flance.tx.netty.container.rooms.ConnectionRoom;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.Map;

public class BizHandlerProxyAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        Object[] args = methodInvocation.getArguments();

        Object nettyData = args[0];
        Object channelData = args[1];
        NettyRequest request;
        NettyResponse response;
        Channel channel;
        
        if (channelData instanceof Channel) {
            channel = (Channel) channelData;
        } else {
            throw new IllegalAccessException("BizHandlerProxyAdvice channel 数据异常");
        }

        if (nettyData instanceof NettyRequest) {
            request = (NettyRequest) nettyData;
            putRequest(request, channel);
        }

        if (nettyData instanceof NettyResponse) {
            response = (NettyResponse) nettyData;
            putResponse(response, channel);
        }

        Object result = null;

        try {
            result = methodInvocation.proceed();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private void putRequest(NettyRequest request, Channel channel) {
        if(null == RoomContainer.getRoom(request.getRoomId())) {
            String roomId = request.getRoomId();
            String roomName = "ping room -" + roomId;
            Map<String, Channel> channelMap = Maps.newConcurrentMap();
            String channelId = request.getRoomId();
            if (null != request.getServerData() && null != request.getServerData().getId()) {
                channelId = request.getServerData().getId();
            }
            channelMap.put(channelId, channel);
            ConnectionRoom connectionRoom = new ConnectionRoom(roomId, roomName, channelMap);
            RoomContainer.createRoom(roomId, connectionRoom);
        } else {
            ConnectionRoom connectionRoom = (ConnectionRoom) RoomContainer.getRoom(request.getRoomId());
            String channelId = request.getRoomId();
            if (null != request.getServerData() && null != request.getServerData().getId()) {
                channelId = request.getServerData().getId();
            }
            if (null == connectionRoom.getChannelById(channelId)) {
                connectionRoom.addChannel(request.getServerData().getId(), channel);
            }
        }
    }

    private void putResponse(NettyResponse response, Channel channel) {
        if(null == RoomContainer.getRoom(response.getRoomId())) {
            String roomId = response.getRoomId();
            String roomName = "pong room -" + roomId;
            Map<String, Channel> channelMap = Maps.newConcurrentMap();
            String channelId = response.getRoomId();
            if (null != response.getServerData() && null != response.getServerData().getId()) {
                channelId = response.getServerData().getId();
            }
            channelMap.put(channelId, channel);
            ConnectionRoom connectionRoom = new ConnectionRoom(roomId, roomName, channelMap);
            RoomContainer.createRoom(roomId, connectionRoom);
        } else {
            ConnectionRoom connectionRoom = (ConnectionRoom) RoomContainer.getRoom(response.getRoomId());
            String channelId = response.getRoomId();
            if (null != response.getServerData() && null != response.getServerData().getId()) {
                channelId = response.getServerData().getId();
            }
            if (null == connectionRoom.getChannelById(channelId)) {
                connectionRoom.addChannel(response.getServerData().getId(), channel);
            }
        }
    }


}
